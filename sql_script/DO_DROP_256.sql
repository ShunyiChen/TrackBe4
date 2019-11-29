DO
$do$
BEGIN 

   FOR i IN 0..255 LOOP
	--RAISE NOTICE '%', i;
	-- 删除transaction表
	EXECUTE format('DROP TABLE IF EXISTS TRANSACTION%I','_'||i);
	-- 删除document1表
	EXECUTE format('DROP TABLE IF EXISTS DOCUMENTS_1%I','_'||i);
	-- 删除document2表
	EXECUTE format('DROP TABLE IF EXISTS TRANSITION%I','_'||i);
      -- 删除document2表
	EXECUTE format('DROP TABLE IF EXISTS USEREVENT%I','_'||i);
   END LOOP;

END
$do$;