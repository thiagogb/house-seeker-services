INSERT INTO provider (id, name, site_url, data_url, mechanism, logo, cron_expression, params, active)
VALUES (10000, 'Oliveira Imóveis', 'https://www.oliveiraimoveissm.com.br', 'https://www.oliveiraimoveissm.com.br/imoveis/a-venda/',
        'JETIMOB_V1', null, '0 0 0 ? * * *', null, 'Y'),
       (10001, 'Cancian Imóveis', 'https://cancianimoveis.com.br', 'https://d3gy91ixuib5mq.cloudfront.net/', 'UNIVERSAL_SOFTWARE', null,
        '0 0 0 ? * * *', null, 'Y'),
       (10002, 'Café Imobiliária', 'https://www.cafeimobiliaria.com.br', 'https://www.cafeimobiliaria.com.br/jsons/', 'SUPER_LOGICA', null,
        '0 0 0 ? * * *', '{"jsonFile":"cafeimoveis.json"}', 'Y'),
       (10003, 'Maiquel Oliveira', 'https://maiqueloliveira.com.br', null, 'JETIMOB_V2', null,
        '0 0 0 ? * * *', '{"subTypes":["Apartamento","Casa","Casa de Condomínio","Cobertura","Sobrado","Terreno"]}', 'Y'),
       (10004, 'Luiz Coelho Imóveis', 'https://www.luizcoelhoimoveis.com.br', null, 'JETIMOB_V3', null, '0 0 0 ? * * *', null, 'Y'),
       (10005, 'InVista Imóveis', 'https://www.invistaimoveissm.com.br', 'https://www.invistaimoveissm.com.br', 'ALAN_WGT', null,
        '0 0 0 ? * * *', '{"hash":"SMJNq9M_zibwamhVExL-Z"}', 'Y'),
       (10006, 'Cotrel Imóveis', 'https://www.cotrelimoveis.com.br', null, 'JETIMOB_V4', null, '0 0 0 ? * * *', null, 'Y');
