const assert = require('assert');
const fs = require('fs');
const path = require('path');

const root = path.resolve(__dirname, '..');
const read = (file) => fs.readFileSync(path.join(root, file), 'utf8');
const walk = (dir) =>
  fs.readdirSync(dir, { withFileTypes: true }).flatMap((entry) => {
    const full = path.join(dir, entry.name);
    return entry.isDirectory() ? walk(full) : [full];
  });

const adminControllerDir = path.join(
  root,
  'yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/admin',
);

const requiredPermissions = new Set();
for (const file of walk(adminControllerDir).filter((item) => item.endsWith('.java'))) {
  const source = fs.readFileSync(file, 'utf8');
  for (const match of source.matchAll(/hasPermission\('([^']+)'\)/g)) {
    if (match[1].startsWith('tutor:')) {
      requiredPermissions.add(match[1]);
    }
  }
}

const menuSql = fs
  .readdirSync(path.join(root, 'sql/mysql'))
  .filter((file) => /^tutor.*\.sql$/.test(file))
  .map((file) => read(`sql/mysql/${file}`))
  .join('\n');

const missing = [...requiredPermissions]
  .filter((permission) => !menuSql.includes(`'${permission}'`))
  .sort();

assert.deepStrictEqual(missing, [], `Missing tutor admin menu permissions: ${missing.join(', ')}`);

const allInOneSql = read('sql/mysql/jiajiao_mysql_all.sql');
const missingInAll = [...requiredPermissions]
  .filter((permission) => !allInOneSql.includes(`'${permission}'`))
  .sort();

assert.deepStrictEqual(
  missingInAll,
  [],
  `Missing tutor admin permissions in jiajiao_mysql_all.sql: ${missingInAll.join(', ')}`,
);

console.log('tutor admin permission SQL contract ok');
