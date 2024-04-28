INSERT INTO urban_property_location (id, id_urban_property, state, city, district, zipcode, street_name, street_number, complement, latitude,
                                     longitude)
VALUES (1369, (select up.id from urban_property up where up.provider_code = '500489'), 'RS', 'Santa Maria', 'Tomazetti', null, null, null, null, -29.720691100,
        -53.794736800),
       (1375, (select up.id from urban_property up where up.provider_code = '2302'), 'RS', 'Santa Maria', 'Nossa Senhora Medianeira', null, null, null, null,
        -29.693526500, -53.803870000),
       (1430, (select up.id from urban_property up where up.provider_code = '98297'), 'RS', 'Santa Maria', 'Nossa Senhora de Fátima', null, null, null, null,
        -29.688865400, -53.814003100),
       (1452, (select up.id from urban_property up where up.provider_code = '145687'), 'RS', 'Santa Maria', 'Camobi', null, null, null, null, -29.725895500,
        -53.799939700),
       (1632, (select up.id from urban_property up where up.provider_code = '3272'), 'RS', 'Santa Maria', 'Patronato', null, null, null, null, -29.682664200,
        -53.829613600);
