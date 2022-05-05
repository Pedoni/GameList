package com.example.mygamelist.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public final class User {
    @PrimaryKey(autoGenerate = true)
    public long userId;
    public final String username;
    public final String userFirstName;
    public final String userLastName;
    public final String userEmail;
    public final String userPassword;
    public final String userImage;
    public final String userBackground;

    public User(final String username, final String userFirstName, final String userLastName, final String userEmail, final String userPassword,
                final String userImage, final String userBackground) {
        this.username = username;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userImage = userImage;
        this.userBackground = userBackground;
    }

    public static User[] populateData() {
        return new User[] {
                new User("pedoni98", "Emanuele", "Lamagna", "emanuele.lamagna@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("pippobenve", "Filippo", "Benvenuti", "filippo.benvenuti3@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("malloc99", "Filippo", "Barbari", "filippo.barbari@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("__dadooo__", "Davide", "Degli Esposti", "davide.degliesposti7@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("dragu_", "Emilian", "Dragusanu", "emilian.dragusanu@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("the_pro", "Samuele", "Turci", "samuele.turci@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("il_bert", "Samuele", "Bertani", "samuele.bertani@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("eleonorotta", "Eleonora", "Bertoni", "eleonora.bertoni@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("lord_frank", "Francesco", "Ballarini", "francesco.ballarini@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("ameliah", "Elisa", "Albertini", "elisa.albertini@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("bernabinoide", "Sara", "Bernabini", "sara.bernabini@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("bartolinipunto", "Lorenzo", "Bartolini", "lorenzo.bartolini@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("uomo_latte", "Michele", "Monti", "michele.monti@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("gayallaplaya", "Gaia", "Ebli", "gaia.ebli@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("danielita", "Daniela", "Chavez Rejas", "daniela.chavez@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("denys_mf", "Denys", "Gruschack", "denys.gruschack@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
                new User("frankiosko", "Francesco", "Esposito", "francesco.esposito@studio.unibo.it",
                        "1234", "ic_user_24", "defaultbackground"),
        };
    }
}