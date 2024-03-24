insert into usuarios (username, password, nombre, apellido, email, enabled, nro_intentos) values ('admin', '$2a$10$cd7BhuNMUJKlr3K4ePeEh.teS.zlZfyiO8n.3WBciBCHnAgTC24Eq', 'Hector', 'Hidalgo', 'h.hidalgo1990@gmail.com', true, 0);
insert into usuarios (username, password, nombre, apellido, email, enabled, nro_intentos) values ('visita', '$2a$10$3k53VwipMS2alXvavU6e..KIPOMpfBZw.TDziSMMk5gEJb.//yDem', 'Andres', 'Espinoza', 'aespinoza1990@gmail.com', true, 0);

insert into roles (nombre) values ('ROLE_USER');
insert into roles (nombre) values ('ROLE_ADMIN');

insert into usuarios_to_roles (usuario_id, role_id) values (1,2);
insert into usuarios_to_roles (usuario_id, role_id) values (1,1);
insert into usuarios_to_roles (usuario_id, role_id) values (2,1);


