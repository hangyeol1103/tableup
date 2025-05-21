SELECT *
FROM businesshour
WHERE bh_rt_num = 1
ORDER BY bh_start;

SELECT * 
FROM reservation 
WHERE res_rt_num = 1;

INSERT INTO businesshour (
  bh_start, bh_end, bh_seat_MAX, bh_seat_current,
  bh_table_MAX, bh_table_current, bh_state, bh_rt_num
) VALUES (
  '2025-05-21 17:00:00', '2025-05-21 23:00:00', 30, 0, 0, 0, TRUE, 1
);


