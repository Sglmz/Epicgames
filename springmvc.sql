-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-08-2025 a las 07:33:09
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `springmvc`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id_rol` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id_rol`, `nombre`) VALUES
(1, 'ADMIN'),
(2, 'DEVELOPER'),
(3, 'USER');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tiendas`
--

CREATE TABLE `tiendas` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `imagen` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tiendas`
--

INSERT INTO `tiendas` (`id`, `nombre`, `descripcion`, `direccion`, `imagen`) VALUES
(1, 'Epic Store', 'Tienda oficial de juegos digitales', 'https://www.epicgames.com', NULL),
(2, 'Indie World', 'Juegos de desarrolladores independientes', 'https://indieworld.com', NULL),
(3, 'RetroLand', 'Clásicos y retro de los 80s y 90s', 'https://retroland.com', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `dinero` double NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `username`, `correo`, `contrasena`, `apellido`, `nombre`, `dinero`) VALUES
(1, 'admin', 'admin@epic.com', 'admin123', 'Nigga', 'Nigga', 500),
(2, 'devjuan', 'juan@dev.com', 'dev123', NULL, '', 250),
(3, 'sofia', 'sofia@user.com', 'user123', NULL, '', 245);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_rol`
--

CREATE TABLE `usuario_rol` (
  `id_usuario` bigint(20) NOT NULL,
  `id_rol` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario_rol`
--

INSERT INTO `usuario_rol` (`id_usuario`, `id_rol`) VALUES
(1, 1),
(2, 2),
(3, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `videojuegos`
--

CREATE TABLE `videojuegos` (
  `id_videojuego` int(11) NOT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `genero` varchar(255) DEFAULT NULL,
  `precio` double NOT NULL,
  `descuento` double NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `id_usuario` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `videojuegos`
--

INSERT INTO `videojuegos` (`id_videojuego`, `titulo`, `descripcion`, `genero`, `precio`, `descuento`, `imagen`, `activo`, `id_usuario`) VALUES
(1, 'Niggerpunk', 'Un shooter futurista en NightCity', 'Acción', 59.99, 0.25, 'https://oyster.ignimgs.com/wordpress/stg.ign.com/2020/03/cyberpunk-2077-update-720x405.jpg', 1, 2),
(2, 'Gta7', 'Juego de fantasía con cartas mágicas', 'Estrategia', 39.99, 0.1, 'https://img.asmedia.epimg.net/resizer/v2/7AHH2SSR75EUFLBELWZKSQ5VZU.png?auth=a24ab473f4a1fac37e78f35f2b717d073f1bcb1167fa5903587373950076db2c&width=1472&height=1104&smart=true', 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `videojuegos_comprados`
--

CREATE TABLE `videojuegos_comprados` (
  `id` int(11) NOT NULL,
  `id_usuario` bigint(20) NOT NULL,
  `id_videojuego` int(11) NOT NULL,
  `fecha_compra` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id_rol`);

--
-- Indices de la tabla `tiendas`
--
ALTER TABLE `tiendas`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- Indices de la tabla `usuario_rol`
--
ALTER TABLE `usuario_rol`
  ADD PRIMARY KEY (`id_usuario`,`id_rol`),
  ADD KEY `id_rol` (`id_rol`);

--
-- Indices de la tabla `videojuegos`
--
ALTER TABLE `videojuegos`
  ADD PRIMARY KEY (`id_videojuego`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `videojuegos_comprados`
--
ALTER TABLE `videojuegos_comprados`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_usuario_videojuego` (`id_usuario`,`id_videojuego`),
  ADD KEY `id_videojuego` (`id_videojuego`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id_rol` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `tiendas`
--
ALTER TABLE `tiendas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `videojuegos`
--
ALTER TABLE `videojuegos`
  MODIFY `id_videojuego` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `videojuegos_comprados`
--
ALTER TABLE `videojuegos_comprados`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `usuario_rol`
--
ALTER TABLE `usuario_rol`
  ADD CONSTRAINT `usuario_rol_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `usuario_rol_ibfk_2` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`) ON DELETE CASCADE;

--
-- Filtros para la tabla `videojuegos`
--
ALTER TABLE `videojuegos`
  ADD CONSTRAINT `videojuegos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL;

--
-- Filtros para la tabla `videojuegos_comprados`
--
ALTER TABLE `videojuegos_comprados`
  ADD CONSTRAINT `videojuegos_comprados_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `videojuegos_comprados_ibfk_2` FOREIGN KEY (`id_videojuego`) REFERENCES `videojuegos` (`id_videojuego`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
