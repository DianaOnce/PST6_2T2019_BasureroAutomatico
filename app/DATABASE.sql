
use pst;

create table Lugar(
	idLugar int unsigned auto_increment,
	ciudad varchar(30),
	latitud float,
	longitud float,
    PRIMARY KEY (idLugar)
);

create table if not exists Usuario(
	idUsuario int unsigned not null auto_increment,
	alias varchar(10),
	pass varbinary(256) not null,
	nombre varchar(30),
    PRIMARY KEY (idUsuario)
);

create table if not exists Sensor(
	idSensor int unsigned not null auto_increment,
	inductor int,
	fotoelectrico float,
    PRIMARY KEY (idSensor)
);

create table if not exists Contenedor(
	idContenedor int unsigned not null auto_increment,
	idLugar int unsigned,
	nivelPlastico int,
	nivelMetal int,
	nivelOrganico int,
    PRIMARY KEY (idContenedor),
    FOREIGN KEY (idLugar) REFERENCES Lugar(idLugar)
);

create table if not exists Registro(
	idRegistro int unsigned not null auto_increment,
	idContenedor int unsigned,
	idSensor int unsigned,
	fecha datetime,
    PRIMARY KEY (idRegistro),
    FOREIGN KEY (idContenedor) REFERENCES Contenedor(idContenedor),
    FOREIGN KEY (idSensor) REFERENCES Sensor(idSensor)
);

create table if not exists Retiro(
	idRetiro int unsigned not null auto_increment,
	idUsuario int unsigned,
	idContenedor int unsigned,
	fecha date,
	tachoPlastico int,
	tachoMetal int,
	tachoOrganico int,
    PRIMARY KEY (idRetiro),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idContenedor) REFERENCES Contenedor(idContenedor)
);



	
	INSERT INTO Usuario VALUES(null,"yo",AES_ENCRYPT("prueba",UNHEX('F3229A0B371ED2D9441B830D21A390C3')),"Jhonston Benjumea");
	INSERT INTO Usuario VALUES(null,"diana",AES_ENCRYPT("prueba",UNHEX('F3229A0B371ED2D9441B830D21A390C3')),"Diana Once");
	INSERT INTO Usuario VALUES(null,"angie",AES_ENCRYPT("prueba",UNHEX('F3229A0B371ED2D9441B830D21A390C3')),"Angie Leon");
	INSERT INTO Usuario VALUES(null,"andre",AES_ENCRYPT("prueba",UNHEX('F3229A0B371ED2D9441B830D21A390C3')),"Andre Icaza");

	INSERT INTO Lugar VALUES(null,"Guayaquil","-2.145476","-79.965636");
	
	INSERT INTO Contenedor VALUES(null,1,0,0,0);
	
	INSERT INTO Sensor VALUES(null,1,120);
	INSERT INTO Sensor VALUES(null,0,1000);
	
	
	INSERT INTO Registro VALUES(null,1,1,"2020-01-19 17:03:15");
	INSERT INTO Registro VALUES(null,1,2,"2020-01-19 18:02:15");



DELIMITER //
CREATE PROCEDURE  user_auth(IN userI VARCHAR(10), IN passI varbinary(256))
BEGIN
		IF (SELECT count(alias) FROM Usuario WHERE alias = userI AND AES_DECRYPT(pass,UNHEX('F3229A0B371ED2D9441B830D21A390C3'))= passI) != 1 THEN
			SET @message_text = CONCAT('Login incorrect!!');
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = @message_text;
		ELSE
			SELECT idUsuario from Usuario where alias = userI;
		END IF;
    
END//
DELIMITER ;


DELIMITER //
CREATE PROCEDURE  getPercen(IN idCont int)
BEGIN

	SELECT nivelPlastico,nivelMetal,nivelOrganico FROM Contenedor WHERE idContenedor=idCont;

END//
DELIMITER ;



DELIMITER //
CREATE PROCEDURE  addRecord(IN inductor int,IN fotoelectrico float,IN tPlastico int,IN tMetal int,IN tOrganico int)
BEGIN

INSERT INTO sensor VALUES(null,inductor,fotoelectrico);

SELECT MAX(idSensor) into @idSens from sensor;

SELECT nivelPlastico into @nPla from contenedor where idContenedor=1;
SELECT nivelMetal into @nMet from contenedor where idContenedor=1;
SELECT nivelOrganico into @nOrg from contenedor where idContenedor=1;


	IF (inductor>=1) THEN

		UPDATE contenedor 

		SET nivelMetal=@nMet+1 WHERE idContenedor=1;

    
	ELSEIF (fotoelectrico>=1) THEN

		UPDATE contenedor 

		SET nivelPlastico=@nPla+1 WHERE idContenedor=1;
		
	ELSE  

		UPDATE contenedor 

		SET nivelOrganico=@nOrg+1 WHERE idContenedor=1;
	
	END if;


		
	IF (tPlastico>=1 OR @nPla>=5) THEN

		UPDATE contenedor 

		SET nivelPlastico=5 WHERE idContenedor=1;

    	END if;
	IF (tMetal>=1 OR @nMet>=5) THEN

		UPDATE contenedor 

		SET nivelMetal=5 WHERE idContenedor=1;

    	END if;
	IF (tOrganico>=1 OR @nOrg>=5) THEN

		UPDATE contenedor 

		SET nivelOrganico=5 WHERE idContenedor=1;

    	END if;

	INSERT INTO registro VALUES(null,1,@idSens,now());

END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE  getRetiro(IN idCont int)
BEGIN

	SELECT retiro.idRetiro as id,
	retiro.fecha,
	retiro.tachoPlastico,
	retiro.tachoMetal,
	retiro.tachoOrganico,
	usuario.nombre
	FROM retiro,usuario 
	WHERE usuario.idUsuario=retiro.idUsuario;

END//
DELIMITER ;






