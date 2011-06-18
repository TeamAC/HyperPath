SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `hyperPath` ;
CREATE SCHEMA IF NOT EXISTS `hyperPath` DEFAULT CHARACTER SET latin1 ;
USE `hyperPath` ;

-- -----------------------------------------------------
-- Table `hyperPath`.`categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`categories` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`categories` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `label` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `label_UNIQUE` (`label` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `hyperPath`.`entities`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`entities` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`entities` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`openingHours`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`openingHours` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`openingHours` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `openTime` TIME NOT NULL ,
  `closeTime` TIME NOT NULL ,
  `days` INT NOT NULL DEFAULT 127 ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`gpslocation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`gpslocation` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`gpslocation` (
  `id` INT NOT NULL ,
  `time` TIME NOT NULL ,
  `latitude` VARCHAR(45) NOT NULL ,
  `longitude` VARCHAR(45) NOT NULL ,
  `altitude` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`advertisers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`advertisers` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`advertisers` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(45) NULL COMMENT '	' ,
  `entities_id` INT NOT NULL ,
  PRIMARY KEY (`id`, `entities_id`) ,
  INDEX `fk_advertisers_entities1` (`entities_id` ASC) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) ,
  CONSTRAINT `fk_advertisers_entities1`
    FOREIGN KEY (`entities_id` )
    REFERENCES `hyperPath`.`entities` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`ads`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`ads` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`ads` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `description` TEXT NOT NULL ,
  `shortDescription` VARCHAR(45) NOT NULL ,
  `startDate` DATETIME NOT NULL ,
  `endDate` DATETIME NOT NULL ,
  `advertisers_id` INT NOT NULL ,
  PRIMARY KEY (`id`, `advertisers_id`) ,
  INDEX `fk_ads_advertisers1` (`advertisers_id` ASC) ,
  CONSTRAINT `fk_ads_advertisers1`
    FOREIGN KEY (`advertisers_id` )
    REFERENCES `hyperPath`.`advertisers` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`services`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`services` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`services` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `label` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(45) NOT NULL ,
  `rating` INT NOT NULL DEFAULT 0 ,
  `categories_id` INT(11) NOT NULL ,
  `entities_id` INT NOT NULL ,
  `openingHours_id` INT NOT NULL ,
  `gpslocation_id` INT NOT NULL ,
  `ads_id` INT NOT NULL ,
  `ads_advertisers_id` INT NOT NULL ,
  PRIMARY KEY (`id`, `categories_id`, `entities_id`, `openingHours_id`, `gpslocation_id`, `ads_id`, `ads_advertisers_id`) ,
  INDEX `fk_services_categories` (`categories_id` ASC) ,
  INDEX `fk_services_entities1` (`entities_id` ASC) ,
  INDEX `fk_services_openingHours1` (`openingHours_id` ASC) ,
  INDEX `fk_services_gpslocation1` (`gpslocation_id` ASC) ,
  INDEX `fk_services_ads1` (`ads_id` ASC, `ads_advertisers_id` ASC) ,
  CONSTRAINT `fk_services_categories`
    FOREIGN KEY (`categories_id` )
    REFERENCES `hyperPath`.`categories` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_services_entities1`
    FOREIGN KEY (`entities_id` )
    REFERENCES `hyperPath`.`entities` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_services_openingHours1`
    FOREIGN KEY (`openingHours_id` )
    REFERENCES `hyperPath`.`openingHours` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_services_gpslocation1`
    FOREIGN KEY (`gpslocation_id` )
    REFERENCES `hyperPath`.`gpslocation` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_services_ads1`
    FOREIGN KEY (`ads_id` , `ads_advertisers_id` )
    REFERENCES `hyperPath`.`ads` (`id` , `advertisers_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `hyperPath`.`clients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`clients` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`clients` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `lastName` VARCHAR(45) NOT NULL ,
  `login` VARCHAR(64) NOT NULL ,
  `password` VARCHAR(45) NOT NULL ,
  `gender` ENUM('Male','Female') NOT NULL ,
  `birthdate` DATE NOT NULL ,
  `entities_id` INT NOT NULL ,
  PRIMARY KEY (`id`, `entities_id`) ,
  INDEX `fk_clients_entities1` (`entities_id` ASC) ,
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) ,
  CONSTRAINT `fk_clients_entities1`
    FOREIGN KEY (`entities_id` )
    REFERENCES `hyperPath`.`entities` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`phones`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`phones` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`phones` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `number` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `number_UNIQUE` (`number` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`faxes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`faxes` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`faxes` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `number` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `number_UNIQUE` (`number` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`emails`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`emails` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`emails` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `address` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `address_UNIQUE` (`address` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`entities_has_faxes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`entities_has_faxes` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`entities_has_faxes` (
  `entities_id` INT NOT NULL ,
  `faxes_id` INT NOT NULL ,
  PRIMARY KEY (`entities_id`, `faxes_id`) ,
  INDEX `fk_entities_has_faxes_faxes1` (`faxes_id` ASC) ,
  INDEX `fk_entities_has_faxes_entities1` (`entities_id` ASC) ,
  CONSTRAINT `fk_entities_has_faxes_entities1`
    FOREIGN KEY (`entities_id` )
    REFERENCES `hyperPath`.`entities` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entities_has_faxes_faxes1`
    FOREIGN KEY (`faxes_id` )
    REFERENCES `hyperPath`.`faxes` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`entities_has_emails`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`entities_has_emails` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`entities_has_emails` (
  `entities_id` INT NOT NULL ,
  `emails_id` INT NOT NULL ,
  PRIMARY KEY (`entities_id`, `emails_id`) ,
  INDEX `fk_entities_has_emails_emails1` (`emails_id` ASC) ,
  INDEX `fk_entities_has_emails_entities1` (`entities_id` ASC) ,
  CONSTRAINT `fk_entities_has_emails_entities1`
    FOREIGN KEY (`entities_id` )
    REFERENCES `hyperPath`.`entities` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entities_has_emails_emails1`
    FOREIGN KEY (`emails_id` )
    REFERENCES `hyperPath`.`emails` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`entities_has_phones`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`entities_has_phones` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`entities_has_phones` (
  `entities_id` INT NOT NULL ,
  `phones_id` INT NOT NULL ,
  PRIMARY KEY (`entities_id`, `phones_id`) ,
  INDEX `fk_entities_has_phones_phones1` (`phones_id` ASC) ,
  INDEX `fk_entities_has_phones_entities1` (`entities_id` ASC) ,
  CONSTRAINT `fk_entities_has_phones_entities1`
    FOREIGN KEY (`entities_id` )
    REFERENCES `hyperPath`.`entities` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entities_has_phones_phones1`
    FOREIGN KEY (`phones_id` )
    REFERENCES `hyperPath`.`phones` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`address` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`address` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `street` VARCHAR(45) NOT NULL ,
  `zip` VARCHAR(45) NOT NULL ,
  `city` VARCHAR(45) NOT NULL ,
  `department` VARCHAR(45) NOT NULL ,
  `country` VARCHAR(45) NOT NULL ,
  `ext` VARCHAR(45) NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `uq_address` (`street` ASC, `zip` ASC, `city` ASC, `department` ASC, `country` ASC, `ext` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`entities_has_address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`entities_has_address` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`entities_has_address` (
  `entities_id` INT NOT NULL ,
  `address_id` INT NOT NULL ,
  PRIMARY KEY (`entities_id`, `address_id`) ,
  INDEX `fk_entities_has_address_address1` (`address_id` ASC) ,
  INDEX `fk_entities_has_address_entities1` (`entities_id` ASC) ,
  CONSTRAINT `fk_entities_has_address_entities1`
    FOREIGN KEY (`entities_id` )
    REFERENCES `hyperPath`.`entities` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entities_has_address_address1`
    FOREIGN KEY (`address_id` )
    REFERENCES `hyperPath`.`address` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`reviews`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`reviews` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`reviews` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `rating` INT NOT NULL ,
  `description` TEXT NOT NULL ,
  `services_id` INT(11) NOT NULL ,
  `clients_id` INT NOT NULL ,
  PRIMARY KEY (`id`, `services_id`, `clients_id`) ,
  INDEX `fk_reviews_services1` (`services_id` ASC) ,
  INDEX `fk_reviews_clients1` (`clients_id` ASC) ,
  CONSTRAINT `fk_reviews_services1`
    FOREIGN KEY (`services_id` )
    REFERENCES `hyperPath`.`services` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reviews_clients1`
    FOREIGN KEY (`clients_id` )
    REFERENCES `hyperPath`.`clients` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `hyperPath`.`clients_bookmarked_services`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hyperPath`.`clients_bookmarked_services` ;

CREATE  TABLE IF NOT EXISTS `hyperPath`.`clients_bookmarked_services` (
  `clients_id` INT NOT NULL ,
  `services_id` INT(11) NOT NULL ,
  PRIMARY KEY (`clients_id`, `services_id`) ,
  INDEX `fk_clients_has_services_services1` (`services_id` ASC) ,
  INDEX `fk_clients_has_services_clients1` (`clients_id` ASC) ,
  CONSTRAINT `fk_clients_has_services_clients1`
    FOREIGN KEY (`clients_id` )
    REFERENCES `hyperPath`.`clients` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_clients_has_services_services1`
    FOREIGN KEY (`services_id` )
    REFERENCES `hyperPath`.`services` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
