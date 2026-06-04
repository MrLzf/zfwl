const assert = require('assert');
const fs = require('fs');
const path = require('path');

const root = path.resolve(__dirname, '..');
const read = (file) => fs.readFileSync(path.join(root, file), 'utf8');
const exists = (file) => fs.existsSync(path.join(root, file));

[
  'sql/mysql/tutor_p2_scale_ops.sql',
  'qa/tutor-p2-load.ps1',
  'yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/util/TutorGeoHashUtils.java',
].forEach((file) => assert.ok(exists(file), `${file} should exist`));

const sql = read('sql/mysql/tutor_p2_scale_ops.sql');
[
  'tutor_trial_time_slot',
  'tutor_trial_appointment',
  'tutor_escrow_trade_order',
  'tutor_teacher_level_record',
  'tutor_recommend_weight_config',
  'geohash',
  'idx_tutor_teacher_resume_geohash',
].forEach((token) => assert.ok(sql.includes(token), `P2 SQL should include ${token}`));

[
  'app/appointment/AppTutorTrialAppointmentController.java',
  'admin/appointment/AdminTutorTrialAppointmentController.java',
  'app/escrow/AppTutorEscrowTradeController.java',
  'admin/escrow/AdminTutorEscrowTradeController.java',
  'admin/recommend/AdminTutorRecommendConfigController.java',
].forEach((file) => {
  const full = `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/${file}`;
  assert.ok(exists(full), `${full} should exist`);
});

const dashboardController = read('yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/admin/dashboard/AdminTutorDashboardController.java');
assert.ok(dashboardController.includes('/trend'), 'dashboard should expose trend endpoint');
assert.ok(dashboardController.includes('/funnel'), 'dashboard should expose funnel endpoint');

const cityController = read('yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/admin/city/AdminTutorCityController.java');
assert.ok(cityController.includes('/rules'), 'city controller should expose rule update endpoint');

const resumeMapper = read('yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal/mysql/resume/TutorTeacherResumeMapper.java');
assert.ok(resumeMapper.includes('tutor_recommend_weight_config'), 'square sorting should read configurable weights');
assert.ok(resumeMapper.includes('geohash'), 'square sorting should support geohash prefilter/index');

console.log('tutor P2 backend contract ok');
