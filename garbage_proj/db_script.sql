CREATE TABLE `garbage` (
  `id` int(11) NOT NULL,
  `lat` DOUBLE NOT NULL,
  `lon` DOUBLE NOT NULL
  `image` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `garbage`
  ADD PRIMARY KEY (`id`);
ALTER TABLE `garbage`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;