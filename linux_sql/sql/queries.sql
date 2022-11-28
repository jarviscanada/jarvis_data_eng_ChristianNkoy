--SQL QUERIES FOR BUSINESS QUESTIONS

--1. Group hosts by hardware info
---- Group hosts by CPU number and sort by their memory size
---- in descending order(within each cpu_number group)
SELECT cpu_number, host_id, total_mem
FROM host_info
GROUP BY cpu_number, host_id, total_mem
ORDER BY cpu_number, total_mem DESC;

--2. Average memory usage
---- Average used memory in percentage over 5 mins interval for each host.
---- (used memory = total memory - free memory).
SELECT host_usage.host_id, hostname, date_trunc('hour', host_usage.timestamp) + date_part('minute', host_usage.timestamp)
    :: int / 5 * interval '5 min' AS time_interval,(AVG(total_mem - memory_free)/SUM(total_mem - memory_free)*100)::int AS avg_pct
    FROM host_usage
    INNER JOIN host_info USING(host_id)
    GROUP BY host_usage.host_id, hostname, time_interval
    ORDER BY host_usage.host_id, time_interval;

--3. Detect host failure
---- The cron job is supposed to insert a new entry to the host_usage table every minute when
---- the server is healthy. We can assume that a server is failed when it inserts less than three
---- data points within 5-min interval. Please write a query to detect host failures.
SELECT host_id, date_trunc('hour', host_usage.timestamp) + date_part('minute', host_usage.timestamp)
    :: int / 5 * interval '5 min' AS time_interval, COUNT(timestamp) AS num_data_entries
    FROM host_usage
    GROUP BY host_id, time_interval
    HAVING  COUNT(timestamp) < 3
    ORDER BY host_id, time_interval;
