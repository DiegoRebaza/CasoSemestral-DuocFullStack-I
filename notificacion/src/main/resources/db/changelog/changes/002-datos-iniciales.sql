-- liquibase formatted sql

-- changeset bravatta:2 labels:seed comment:Insertar datos de prueba en notificacion
INSERT INTO notificacion (id_cliente, tipo, evento, mensaje, estado, fecha_envio) VALUES
(1, 'SMS', 'COMPRA_REALIZADA',  'Tu compra #1 ha sido registrada exitosamente. ¡Gracias por preferir Bravatta!', 'ENVIADO', NOW()),
(2, 'SMS', 'COMPRA_REALIZADA',  'Tu compra #2 ha sido registrada exitosamente. ¡Gracias por preferir Bravatta!', 'ENVIADO', NOW()),
(3, 'SMS', 'CUPON_CUMPLEANOS',  '¡Feliz cumpleaños! Tienes un cupón especial esperándote en Bravatta. ¡Ven a disfrutarlo!', 'ENVIADO', NOW()),
(4, 'SMS', 'COMPRA_REALIZADA',  'Tu compra #3 ha sido registrada exitosamente. ¡Gracias por preferir Bravatta!', 'ENVIADO', NOW()),
(5, 'SMS', 'CUPON_CUMPLEANOS',  '¡Feliz cumpleaños! Tienes un cupón especial esperándote en Bravatta. ¡Ven a disfrutarlo!', 'ENVIADO', NOW());