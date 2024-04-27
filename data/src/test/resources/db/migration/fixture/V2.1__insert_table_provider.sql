INSERT INTO provider (id, name, site_url, data_url, mechanism, logo, cron_expression, params, active)
VALUES (nextval('provider_seq'), 'Oliveira Imóveis', 'https://www.oliveiraimoveissm.com.br', 'https://www.oliveiraimoveissm.com.br/imoveis/a-venda/',
        'JETIMOB_V1', null, '0 0 9 ? * MON,WED,FRI *', null, 'N'),
       (nextval('provider_seq'), 'Cancian Imóveis', 'https://cancianimoveis.com.br', 'https://d3gy91ixuib5mq.cloudfront.net/', 'UNIVERSAL_SOFTWARE', null,
        '0 0 9 ? * MON,WED,FRI *', null, 'N'),
       (nextval('provider_seq'), 'Café Imobiliária', 'https://www.cafeimobiliaria.com.br', 'https://www.cafeimobiliaria.com.br/jsons/', 'SUPER_LOGICA', null,
        '0 0 9 ? * MON,WED,FRI *', '{"jsonFile":"cafeimoveis.json"}', 'N'),
       (nextval('provider_seq'), 'Maiquel Oliveira', 'https://maiqueloliveira.com.br', null, 'JETIMOB_V2', null,
        '0 0 9 ? * MON,WED,FRI *', '{"subTypes":["Apartamento","Casa","Casa de Condomínio","Cobertura","Sobrado","Terreno"]}', 'N'),
       (nextval('provider_seq'), 'Luiz Coelho Imóveis', 'https://www.luizcoelhoimoveis.com.br', null, 'JETIMOB_V3', null, '0 0 9 ? * MON,WED,FRI *', null, 'N'),
       (nextval('provider_seq'), 'InVista Imóveis', 'https://www.invistaimoveissm.com.br', 'https://www.invistaimoveissm.com.br', 'ALAN_WGT', null,
        '0 0 9 ? * MON,WED,FRI *', '{"hash":"SMJNq9M_zibwamhVExL-Z"}', 'N'),
       (nextval('provider_seq'), 'Cotrel Imóveis', 'https://www.cotrelimoveis.com.br', null, 'JETIMOB_V4', null, '0 0 9 ? * MON,WED,FRI *', null, 'N');
