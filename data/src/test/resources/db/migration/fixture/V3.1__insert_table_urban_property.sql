DO
$$
    DECLARE
        id_provider INT;
    BEGIN
        select p.id
        into id_provider
        from provider p
        where p.name = 'Oliveira Imóveis';

        INSERT INTO urban_property (id, id_provider, provider_code, url, contract, type, sub_type, dormitories, suites, bathrooms, garages, sell_price,
                                    sell_price_variation, rent_price, rent_price_variation, condominium_price, condominium_price_variation, condominium_name,
                                    exchangeable, status, financeable, occupied, notes, creation_date, last_analysis_date, exclusion_date, analyzable)
        VALUES (nextval('urban_property_seq'), id_provider, '500489',
                'https://www.oliveiraimoveissm.com.br/imovel/500489', 'SELL', 'RESIDENTIAL', 'Casa de Condomínio', 4, 3, 3, 2, 1800000.00, null, 1800000.00,
                null, null, null, '', null, null, null, null, 'A sua nova casa em um condomínio com segurança e infraestrutura completa.',
                '2024-04-27 12:05:14.685450', '2024-04-27 12:05:14.685465', null, 'Y'),
               (nextval('urban_property_seq'), id_provider, '2302',
                'https://www.oliveiraimoveissm.com.br/imovel/2302', 'SELL', 'RESIDENTIAL', 'Apartamento', 3, 2, 3, 2, 1166000.00, null, 1166000.00, null, null,
                null, '', null, null, null, null,
                'Apartamento amplo com 3 dormitórios sendo 2 suites e 2 vagas de garagem, localizado em uma das regiões mais procuradas de Santa Maria, condomínio com infraestrutura completa',
                '2024-04-27 12:05:14.685699', '2024-04-27 12:05:14.685712', null, 'Y'),
               (nextval('urban_property_seq'), id_provider, '98297',
                'https://www.oliveiraimoveissm.com.br/imovel/98297', 'SELL', 'RESIDENTIAL', 'Cobertura', 3, 1, 3, 2, 960000.00, null, 960000.00, null, null,
                null, '', null, null, null, null,
                'Cobertura diferenciada das demais no Centro de Santa Maria, possui uma distribuição rara, seus cômodos espaçosos, e bem iluminados.',
                '2024-04-27 12:05:14.688103', '2024-04-27 12:05:14.688118', null, 'Y'),
               (nextval('urban_property_seq'), id_provider, '145687',
                'https://www.oliveiraimoveissm.com.br/imovel/145687', 'SELL', 'RESIDENTIAL', 'Casa de Condomínio', 3, 1, 2, 2, 1180000.00, null, 1180000.00,
                null, null, null, '', null, null, null, null, 'Você acaba de encontrar a casa dos seus sonhos!', '2024-04-27 12:05:14.689251',
                '2024-04-27 12:05:14.689267', null, 'Y'),
               (nextval('urban_property_seq'), id_provider, '3272',
                'https://www.oliveiraimoveissm.com.br/imovel/3272', 'SELL', 'RESIDENTIAL', 'Casa', 4, 1, 3, 4, 1590000.00, null, 1590000.00, null, null, null,
                '', null, null, null, null, 'Casa Duplex Espetacular com 4 Dormitórios, Piscina e Edícula!', '2024-04-27 12:05:14.696367',
                '2024-04-27 12:05:14.696376', null, 'Y');
    END
$$;