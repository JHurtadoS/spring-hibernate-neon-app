--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- Name: update_actualizado_en_column(); Type: FUNCTION; Schema: public; Owner: neondb_owner
--

CREATE FUNCTION public.update_actualizado_en_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
   NEW.actualizado_en = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$;


ALTER FUNCTION public.update_actualizado_en_column() OWNER TO neondb_owner;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: categorias; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.categorias (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    nombre character varying(255) NOT NULL,
    descripcion text,
    creado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    actualizado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT true
);


ALTER TABLE public.categorias OWNER TO neondb_owner;

--
-- Name: clientes; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.clientes (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    nombre character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    telefono character varying(50),
    direccion text,
    creado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    actualizado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT true
);


ALTER TABLE public.clientes OWNER TO neondb_owner;

--
-- Name: empresas; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.empresas (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    nombre character varying(255) NOT NULL,
    direccion text,
    telefono character varying(50),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT true,
    nit character varying(255)
);


ALTER TABLE public.empresas OWNER TO neondb_owner;

--
-- Name: ordenes; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.ordenes (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    fecha timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    total numeric(10,2) NOT NULL,
    cliente_id uuid NOT NULL,
    empresa_id uuid NOT NULL,
    creado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    actualizado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT true
);


ALTER TABLE public.ordenes OWNER TO neondb_owner;

--
-- Name: ordenes_productos; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.ordenes_productos (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    orden_id uuid NOT NULL,
    producto_id uuid NOT NULL,
    cantidad integer NOT NULL,
    precio numeric(10,2) NOT NULL,
    estado boolean DEFAULT true
);


ALTER TABLE public.ordenes_productos OWNER TO neondb_owner;

--
-- Name: producto_categorias; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.producto_categorias (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    producto_id uuid NOT NULL,
    categoria_id uuid NOT NULL,
    creado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    actualizado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    estado boolean DEFAULT true
);


ALTER TABLE public.producto_categorias OWNER TO neondb_owner;

--
-- Name: productos; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.productos (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    nombre character varying(255) NOT NULL,
    descripcion text,
    precio numeric(10,2) NOT NULL,
    stock integer NOT NULL,
    creado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    actualizado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    empresa_id uuid NOT NULL,
    estado boolean DEFAULT true
);


ALTER TABLE public.productos OWNER TO neondb_owner;

--
-- Name: user_profiles; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.user_profiles (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    name character varying(255),
    phone character varying(50),
    theme_preference character varying(50),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    user_id uuid NOT NULL,
    is_admin boolean DEFAULT false
);


ALTER TABLE public.user_profiles OWNER TO neondb_owner;

--
-- Name: users; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.users (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.users OWNER TO neondb_owner;

--
-- Data for Name: categorias; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

INSERT INTO public.categorias (id, nombre, descripcion, creado_en, actualizado_en, estado) VALUES ('ce8511b4-1cd4-4105-955a-f5bbce29d6f1', 'Lacteos', 'Lacteos (leche , yogures, etc) y refrigerados descripcion', NULL, '2025-01-15 06:15:00.354103', true);
INSERT INTO public.categorias (id, nombre, descripcion, creado_en, actualizado_en, estado) VALUES ('ce56cbd6-47a0-4e37-b407-cc6e5cb6b48c', 'Lacteos Prueba disable', 'Lacteos descripcion disable', NULL, '2025-01-15 06:15:46.885533', false);
INSERT INTO public.categorias (id, nombre, descripcion, creado_en, actualizado_en, estado) VALUES ('180c030d-731a-4722-b3bd-d9659d51e48d', 'Aseo', 'Aseo descripcion', NULL, NULL, true);
INSERT INTO public.categorias (id, nombre, descripcion, creado_en, actualizado_en, estado) VALUES ('48a7e697-e55c-420a-aeee-02d0b65a7aa3', 'Carnes', 'Carnes descripcion', NULL, NULL, true);
INSERT INTO public.categorias (id, nombre, descripcion, creado_en, actualizado_en, estado) VALUES ('92f2e90c-b621-4245-a68a-f7f95727c47d', 'Cereales', 'Cereales descripcion', NULL, NULL, true);


--
-- Data for Name: clientes; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--



--
-- Data for Name: empresas; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

INSERT INTO public.empresas (id, nombre, direccion, telefono, created_at, updated_at, estado, nit) VALUES ('176ec08e-cb31-4fc8-aa3e-3688aab8b3dc', 'Asadero do├▒a juana', 'Cr 20', '3216248871', '2025-01-15 01:52:03.742991', '2025-01-15 01:52:03.742991', true, '1122');
INSERT INTO public.empresas (id, nombre, direccion, telefono, created_at, updated_at, estado, nit) VALUES ('0b51d4d9-481e-4225-87f9-04f7e2a730d9', '1111', '1111', '11111', '2025-01-15 00:36:50.79127', '2025-01-15 00:36:50.79127', false, 'Holaa');
INSERT INTO public.empresas (id, nombre, direccion, telefono, created_at, updated_at, estado, nit) VALUES ('eef6d77b-33fd-46d6-81b4-128f87cc10e0', 'Empresa 2', NULL, NULL, '2025-01-15 01:51:08.855127', '2025-01-15 01:51:08.855127', true, NULL);
INSERT INTO public.empresas (id, nombre, direccion, telefono, created_at, updated_at, estado, nit) VALUES ('d0cb8907-599d-4615-bed0-eeea04349a01', 'Empresa creadp desde app', 'Cr 88 #44', '311002211', '2025-01-17 05:44:35.395568', '2025-01-17 05:44:35.39557', true, '100101');
INSERT INTO public.empresas (id, nombre, direccion, telefono, created_at, updated_at, estado, nit) VALUES ('32217421-2d27-43ee-aebb-10ee0ee83a9c', 'Empresa desahabilitar ', 'Cr 88 #44 22', '311002222', '2025-01-17 05:54:07.258886', '2025-01-17 05:54:07.258887', false, '112221');
INSERT INTO public.empresas (id, nombre, direccion, telefono, created_at, updated_at, estado, nit) VALUES ('9700d183-a88f-4462-8505-791cb1f9ad58', 'Empresa creada desde app 5 editada', NULL, NULL, '2025-01-17 05:46:24.975846', '2025-01-17 05:46:24.975847', false, NULL);
INSERT INTO public.empresas (id, nombre, direccion, telefono, created_at, updated_at, estado, nit) VALUES ('c86af733-2fe0-4977-8a70-b43c611a2a53', 'Empresas de carne', 'Cr 69 #55 66', '1112111', '2025-01-17 21:16:03.46242', '2025-01-17 21:16:03.462421', true, '1222');
INSERT INTO public.empresas (id, nombre, direccion, telefono, created_at, updated_at, estado, nit) VALUES ('4e3b3ad4-39a3-47ed-9520-71ca3bdd3313', 'Nueva', 'Dire 44', '11112211', '2025-01-17 21:27:04.275955', '2025-01-17 21:27:04.275956', true, '111');
INSERT INTO public.empresas (id, nombre, direccion, telefono, created_at, updated_at, estado, nit) VALUES ('8b750d30-261a-4a00-a280-8f435780c961', 'Empresa de pepito actualizado 2', NULL, '3111211', '2025-01-17 05:53:00.263836', '2025-01-17 05:53:00.263836', false, NULL);
INSERT INTO public.empresas (id, nombre, direccion, telefono, created_at, updated_at, estado, nit) VALUES ('a9d4699d-00f5-4464-8cfa-a9b5b7bdfe9a', 'Empresas 111', '111', '111222', '2025-01-17 21:28:27.293638', '2025-01-17 21:28:27.293639', true, '222');


--
-- Data for Name: ordenes; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--



--
-- Data for Name: ordenes_productos; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--



--
-- Data for Name: producto_categorias; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

INSERT INTO public.producto_categorias (id, producto_id, categoria_id, creado_en, actualizado_en, estado) VALUES ('c458fdb9-9eb0-436a-a305-0c5bad2e8754', '3d2a4025-1d74-4130-a55a-83509f03882c', '180c030d-731a-4722-b3bd-d9659d51e48d', NULL, NULL, true);
INSERT INTO public.producto_categorias (id, producto_id, categoria_id, creado_en, actualizado_en, estado) VALUES ('ac15bf0d-c431-407e-ba37-1c9cce78f320', '53716f8c-f59d-4003-a8ef-9c67bd018b9e', '48a7e697-e55c-420a-aeee-02d0b65a7aa3', NULL, NULL, true);
INSERT INTO public.producto_categorias (id, producto_id, categoria_id, creado_en, actualizado_en, estado) VALUES ('0247d98a-c72d-4eb9-8be2-e3a0cd921aeb', '53716f8c-f59d-4003-a8ef-9c67bd018b9e', '92f2e90c-b621-4245-a68a-f7f95727c47d', NULL, NULL, true);


--
-- Data for Name: productos; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

INSERT INTO public.productos (id, nombre, descripcion, precio, stock, creado_en, actualizado_en, empresa_id, estado) VALUES ('1e6d7770-b751-4eaa-8e4b-fd92e4023d28', 'Prueba 3', 'Descripcion 3', 212.00, 121, NULL, NULL, '176ec08e-cb31-4fc8-aa3e-3688aab8b3dc', true);
INSERT INTO public.productos (id, nombre, descripcion, precio, stock, creado_en, actualizado_en, empresa_id, estado) VALUES ('3d2a4025-1d74-4130-a55a-83509f03882c', 'Producto 1', 'Descripcion', 200.00, 100, NULL, NULL, '176ec08e-cb31-4fc8-aa3e-3688aab8b3dc', true);
INSERT INTO public.productos (id, nombre, descripcion, precio, stock, creado_en, actualizado_en, empresa_id, estado) VALUES ('9064296a-ffba-41f0-9378-927a9f5199aa', 'Producto 3', 'Descripcion 3', 400.00, 2, NULL, NULL, '176ec08e-cb31-4fc8-aa3e-3688aab8b3dc', true);
INSERT INTO public.productos (id, nombre, descripcion, precio, stock, creado_en, actualizado_en, empresa_id, estado) VALUES ('a7f47c2a-c8e0-4aaa-ad22-6301f9e156e4', 'Prueba 2', 'Descripcion 2', 222.00, 111, NULL, NULL, '176ec08e-cb31-4fc8-aa3e-3688aab8b3dc', true);
INSERT INTO public.productos (id, nombre, descripcion, precio, stock, creado_en, actualizado_en, empresa_id, estado) VALUES ('53716f8c-f59d-4003-a8ef-9c67bd018b9e', 'Prueba 4', 'Descripcion 4', 214.00, 124, NULL, NULL, '176ec08e-cb31-4fc8-aa3e-3688aab8b3dc', true);
INSERT INTO public.productos (id, nombre, descripcion, precio, stock, creado_en, actualizado_en, empresa_id, estado) VALUES ('a1c95bf9-1af5-427e-8c9a-6ae2eb499871', 'Producto cambiado 2', 'Descripcion 2', 300.00, 10, NULL, NULL, '176ec08e-cb31-4fc8-aa3e-3688aab8b3dc', false);
INSERT INTO public.productos (id, nombre, descripcion, precio, stock, creado_en, actualizado_en, empresa_id, estado) VALUES ('a2a1af5b-c847-496b-830f-e0f044c0391f', 'Carne de hamburguesa zenu', 'carnes 250gg', 20000.00, 100, NULL, NULL, '176ec08e-cb31-4fc8-aa3e-3688aab8b3dc', true);


--
-- Data for Name: user_profiles; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

INSERT INTO public.user_profiles (id, name, phone, theme_preference, created_at, updated_at, user_id, is_admin) VALUES ('8c90ce7e-f6a6-4a95-8685-c6ccbec0f2cc', 'Test User', '1234567890', 'dark', '2025-01-14 16:26:16.264545', '2025-01-14 16:26:16.264545', '10e41992-507e-436a-8399-2ba08c95bc1a', true);
INSERT INTO public.user_profiles (id, name, phone, theme_preference, created_at, updated_at, user_id, is_admin) VALUES ('e522eb11-5994-403d-bfb6-254800977493', 'juan', '123456', 'dark', NULL, NULL, '9bc4bba9-ca97-419f-a188-4a3b0bed116e', true);
INSERT INTO public.user_profiles (id, name, phone, theme_preference, created_at, updated_at, user_id, is_admin) VALUES ('3d3a78b1-ed1e-4795-8f14-08ac06e177e4', 'Pepito', '311556677', 'dark', NULL, NULL, 'f7400929-d9b3-46fb-b8db-cd8d824fd776', true);
INSERT INTO public.user_profiles (id, name, phone, theme_preference, created_at, updated_at, user_id, is_admin) VALUES ('91610d6e-b0cc-4d11-9977-7075e8551a69', 'Juan little', '31152221', 'dark', NULL, NULL, 'f8f0aaac-a0df-452a-9f47-27ab855a00de', false);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

INSERT INTO public.users (id, email, password, created_at, updated_at) VALUES ('10e41992-507e-436a-8399-2ba08c95bc1a', 'testuser@example.com', 'securepassword', '2025-01-14 16:25:36.141113', '2025-01-14 16:25:36.141113');
INSERT INTO public.users (id, email, password, created_at, updated_at) VALUES ('9bc4bba9-ca97-419f-a188-4a3b0bed116e', 'juan@test.com', '$2a$10$tgfUxfG7Zsc24YGuaulY5O4cJmuJ5BeFfekEyKgGDUiw.68fMBz62', NULL, NULL);
INSERT INTO public.users (id, email, password, created_at, updated_at) VALUES ('f7400929-d9b3-46fb-b8db-cd8d824fd776', 'pepitoSuarez@gmail.com', '$2a$10$PLLAl7hgbQPK.T0tf./kveraH.TIS0OtJGml6M.ej/infZBmDKqGy', NULL, NULL);
INSERT INTO public.users (id, email, password, created_at, updated_at) VALUES ('f8f0aaac-a0df-452a-9f47-27ab855a00de', 'juan@gmail.com', '$2a$10$vK0l4rAcJlh0d/6et1kgIuJd0RbiUbZtywVpuVqVos31m098xvIKO', NULL, NULL);


--
-- Name: categorias categorias_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (id);


--
-- Name: clientes clientes_email_key; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_email_key UNIQUE (email);


--
-- Name: clientes clientes_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_pkey PRIMARY KEY (id);


--
-- Name: empresas empresas_nit_key; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.empresas
    ADD CONSTRAINT empresas_nit_key UNIQUE (nit);


--
-- Name: empresas empresas_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.empresas
    ADD CONSTRAINT empresas_pkey PRIMARY KEY (id);


--
-- Name: ordenes ordenes_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.ordenes
    ADD CONSTRAINT ordenes_pkey PRIMARY KEY (id);


--
-- Name: ordenes_productos ordenes_productos_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.ordenes_productos
    ADD CONSTRAINT ordenes_productos_pkey PRIMARY KEY (id);


--
-- Name: producto_categorias producto_categorias_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.producto_categorias
    ADD CONSTRAINT producto_categorias_pkey PRIMARY KEY (id);


--
-- Name: productos productos_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_pkey PRIMARY KEY (id);


--
-- Name: user_profiles user_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_pkey PRIMARY KEY (id);


--
-- Name: user_profiles user_profiles_user_id_key; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_user_id_key UNIQUE (user_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: categorias set_actualizado_en_categorias; Type: TRIGGER; Schema: public; Owner: neondb_owner
--

CREATE TRIGGER set_actualizado_en_categorias BEFORE UPDATE ON public.categorias FOR EACH ROW EXECUTE FUNCTION public.update_actualizado_en_column();


--
-- Name: producto_categorias set_actualizado_en_producto_categorias; Type: TRIGGER; Schema: public; Owner: neondb_owner
--

CREATE TRIGGER set_actualizado_en_producto_categorias BEFORE UPDATE ON public.producto_categorias FOR EACH ROW EXECUTE FUNCTION public.update_actualizado_en_column();


--
-- Name: ordenes fk_cliente; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.ordenes
    ADD CONSTRAINT fk_cliente FOREIGN KEY (cliente_id) REFERENCES public.clientes(id) ON DELETE CASCADE;


--
-- Name: productos fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES public.empresas(id) ON DELETE CASCADE;


--
-- Name: ordenes fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.ordenes
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES public.empresas(id) ON DELETE CASCADE;


--
-- Name: ordenes_productos fk_orden; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.ordenes_productos
    ADD CONSTRAINT fk_orden FOREIGN KEY (orden_id) REFERENCES public.ordenes(id) ON DELETE CASCADE;


--
-- Name: ordenes_productos fk_producto; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.ordenes_productos
    ADD CONSTRAINT fk_producto FOREIGN KEY (producto_id) REFERENCES public.productos(id) ON DELETE CASCADE;


--
-- Name: producto_categorias producto_categorias_categoria_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.producto_categorias
    ADD CONSTRAINT producto_categorias_categoria_id_fkey FOREIGN KEY (categoria_id) REFERENCES public.categorias(id) ON DELETE CASCADE;


--
-- Name: producto_categorias producto_categorias_producto_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.producto_categorias
    ADD CONSTRAINT producto_categorias_producto_id_fkey FOREIGN KEY (producto_id) REFERENCES public.productos(id) ON DELETE CASCADE;


--
-- Name: user_profiles user_profiles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.user_profiles
    ADD CONSTRAINT user_profiles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: public; Owner: cloud_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE cloud_admin IN SCHEMA public GRANT ALL ON SEQUENCES TO neon_superuser WITH GRANT OPTION;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: cloud_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE cloud_admin IN SCHEMA public GRANT ALL ON TABLES TO neon_superuser WITH GRANT OPTION;


--
-- PostgreSQL database dump complete
--

