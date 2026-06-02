-- ============================================================
-- Menu: chong zhi ji lu (wallet recharge records)
-- ============================================================

INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted) VALUES ('充值记录', '', 2, 3, 2551, 'wallet-recharge', 'ep:list', 'pay/wallet/recharge/index', 'WalletRecharge', 0, b'1', b'1', b'1', '', '2026-06-02 12:00:00', '', '2026-06-02 12:00:00', b'0');
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted) VALUES ('充值记录查询', 'pay:wallet-recharge:query', 3, 1, NULL, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2026-06-02 12:00:00', '', '2026-06-02 12:00:00', b'0');
