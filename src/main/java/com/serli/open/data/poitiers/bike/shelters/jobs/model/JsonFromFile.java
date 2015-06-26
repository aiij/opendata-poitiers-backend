package com.serli.open.data.poitiers.bike.shelters.jobs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by chris on 04/05/15.
 */
public class JsonFromFile {
    public String type;
    public JsonFromFileProperties properties;
    public JsonFromFileGeometry geometry;

    public static class JsonFromFileProperties {
        @JsonProperty("gml_id")
        public String gmlId;
        @JsonProperty("OBJECTID")
        public int objectId;
        @JsonProperty("TYPE_DE_STATIONNEMENT")
        public String shelterType;
        @JsonProperty("NOMBRE")
        public int capacity;
        @JsonProperty("ETAT")
        public String state;
        @JsonProperty("DATE")
        public Date date;
        @JsonProperty("EQUIPEMENT_GP")
        public String equipementGP;
        @JsonProperty("NUM_HORODATEUR")
        public int numberTicketmachine;
        @JsonProperty("Identifiant")
        public String identifiant;
        @JsonProperty("QUARTIER")
        public String quartier;
        @JsonProperty("CLASSEMENT")
        public int rank;
        @JsonProperty("Observation")
        public String observation;
        @JsonProperty("Commune")
        public String city;
        @JsonProperty("Thème")
        public String theme;
        @JsonProperty("Sous_thème")
        public String underTheme;
        @JsonProperty("Identifiant_de_l_équipement")
        public int equipIdentifiant;
        @JsonProperty("Date_de_mise_à_jour")
        public String dateMAJ;
        @JsonProperty("Nom_de_l_équipement")
        public String equipName;
        @JsonProperty("Adresse")
        public String address;
        @JsonProperty("Code_postal")
        public int postCode;
        @JsonProperty("Quartier")
        public String quartierBis;
        @JsonProperty("Téléphone")
        public String phone;
        @JsonProperty("Fax")
        public String fax;
        @JsonProperty("E-mail")
        public String mail;
        @JsonProperty("Site_web")
        public String web;
        @JsonProperty("Horaires_d_ouvertures")
        public String openedTime;
        @JsonProperty("Gestionnaire")
        public String gestionnaire;
        @JsonProperty("Informations_complémentaires")
        public String moreInformation;
        @JsonProperty("Lien_html")
        public String html;
        @JsonProperty("Lien_photo")
        public String picture;
        @JsonProperty("Identifiant_bâtiment")
        public String idBuilding;
        @JsonProperty("Statut_de_l_équipement")
        public String equipState;

    }

    public static class JsonFromFileGeometry {
        public String type;
        public double[] coordinates;
    }
}
