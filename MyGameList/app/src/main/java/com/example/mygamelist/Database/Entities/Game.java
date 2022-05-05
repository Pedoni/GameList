package com.example.mygamelist.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mygamelist.Database.GameDescriptions;

@Entity(tableName = "game")
public final class Game {
    @PrimaryKey(autoGenerate = true)
    public long gameId;
    public final String gameName;
    public final String publisher;
    public final String developer;
    public final String publishYear;
    public final String publishMonth;
    public final String publishDay;
    public final String platform;
    public final String series;
    public final String gameImage;
    public final String gamePanoramic;
    public final String description;
    public final String gender1;
    public final String gender2;
    public final String gender3;
    public final boolean released;

    public Game(final String gameName, final String publisher, final String developer, final String publishYear, final String publishMonth, final String publishDay,
                final String platform, final String series, final String gameImage, final String gamePanoramic, final String description, final String gender1,
                final String gender2, final String gender3, final boolean released) {
        this.gameName = gameName;
        this.publisher = publisher;
        this.developer = developer;
        this.publishYear = publishYear;
        this.publishMonth = publishMonth;
        this.publishDay = publishDay;
        this.platform = platform;
        this.series = series;
        this.gameImage = gameImage;
        this.gamePanoramic = gamePanoramic;
        this.description = description;
        this.gender1 = gender1;
        this.gender2 = gender2;
        this.gender3 = gender3;
        this.released = released;
    }

    public static Game[] populateData() {
        return new Game[] {
                new Game("Pokémon Diamond Version", "Nintendo", "GameFreak","2006", "9",
                        "28","Nintendo DS", "Pokémon", "pokemondiamond", "pokemondiamond_p",
                        GameDescriptions.get("Pokémon Diamond Version"), "Adventure", "RPG", "Fantasy", true),
                new Game("Call Of Duty Black Ops 2", "Activision Blizzard", "Treyarch","2012", "11",
                        "12","Playstation 3", "Call Of Duty", "codbo2", "codbo2_p",
                        GameDescriptions.get("Call Of Duty Black Ops 2"),"FPS","Action", "War", true),
                new Game("Far Cry 3", "Ubisoft", "Ubisoft Montreal","2012", "11", "29",
                        "Xbox 360", "Far Cry", "farcry3", "farcry3_p",
                        GameDescriptions.get("Far Cry 3"), "FPS", "Open world", "Adventure", true),
                new Game("Super Mario Odyssey", "Nintendo", "Nintendo EPD","2017", "10", "27",
                        "Nintendo Switch", "Super Mario", "supermarioodyssey", "supermarioodyssey_p",
                        GameDescriptions.get("Super Mario Odyssey"), "Platform", "Open world", "Puzzle", true),
                new Game("Fifa 21", "EA", "EA Canada","2020", "10", "5",
                        "Playstation 5", "Fifa", "fifa21", "fifa21_p",
                        GameDescriptions.get("Fifa 21"), "Sport", "Soccer", "Simulation", true),
                new Game("Assassin's Creed 3", "Ubisoft", "Ubisoft Montreal","2012", "10", "30",
                        "Xbox One", "Assassin's Creed", "ac3", "ac3_p",
                        GameDescriptions.get("Assassin's Creed 3"), "Adventure", "Stealth", "Open world", true),
                new Game("Grand Theft Auto 5", "Rockstar", "Rockstar North","2013", "9", "17",
                        "Xbox One", "Grand Theft Auto", "gta5", "gta5_p",
                        GameDescriptions.get("Grand Theft Auto 5"), "Adventure", "Open world", "Action", true),
                new Game("Inazuma Eleven 3: Lightning Bolt", "Level-5", "Level-5","2013", "9", "27",
                        "Nintendo 3DS", "Inazuma Eleven", "ie3lb", "ie3lb_p",
                        GameDescriptions.get("Inazuma Eleven 3: Lightning Bolt"), "Soccer", "RPG", "Fantasy", true),
                new Game("Pokémon Pearl Version", "Nintendo", "GameFreak","2006", "9", "28",
                        "Nintendo DS", "Pokémon", "pokemonpearl", "pokemondiamond_p",
                        GameDescriptions.get("Pokémon Pearl Version"), "Adventure", "RPG", "Fantasy", true),
                new Game("Far Cry 6", "Ubisoft", "Ubisoft Montreal","2021", "12", "30",
                        "Playstation 5", "Far Cry", "farcry6", "farcry6_p",
                        GameDescriptions.get("Far Cry 6"), "FPS", "Open world", "Adventure", false),
                new Game("Biomutant", "THQ Nordic", "Experiment 101","2021", "5", "25",
                        "Playstation 5", "Biomutant", "biomutant", "biomutant_p",
                        GameDescriptions.get("Biomutant"), "RPG", "Post-apocalyctic", "Adventure", true),
                new Game("Pokémon Legends: Arceus", "Nintendo", "GameFreak","2022", "1", "30",
                        "Nintendo Switch", "Pokémon", "pokemonarceus", "pokemonarceus",
                        GameDescriptions.get("Pokémon Legends: Arceus"), "Adventure", "RPG", "Fantasy", true),
                new Game("The Legend of Zelda: Breath Of The Wild", "Nintendo", "Nintendo EPD","2017", "3",
                        "3", "Nintendo Switch", "The Legend of Zelda", "zeldabotw", "zeldabotw_p",
                        GameDescriptions.get("The Legend of Zelda: BOTW"), "Adventure", "RPG", "Fantasy", true),
                new Game("The Legend of Zelda: Skyward Sword HD", "Nintendo", "Nintendo EAD","2021", "7",
                        "16", "Nintendo Switch", "The Legend of Zelda", "zeldasksw", "zeldasksw_p",
                        GameDescriptions.get("The Legend of Zelda: Skyward Sword HD"), "Adventure", "RPG", "Fantasy", false),
                new Game("Resident Evil Village", "Capcom", "Capcom","2021", "4", "18",
                        "Xbox Series X", "Resident Evil", "residentevilvillage", "residentevilvillage_p",
                        GameDescriptions.get("Resident Evil Village"), "Survival", "Horror", "FPS", true),
                new Game("Monster Hunter Rise", "Capcom", "Capcom","2021", "3", "26",
                        "Nintendo Switch", "Monster Hunter", "monsterhunterrise", "monsterhunterrise_p",
                        GameDescriptions.get("Monster Hunter Rise"), "Fantasy", "Action", "RPG", true),
                new Game("Starfield", "Bethesda", "Bethesda","2022", "11", "11",
                        "Xbox Series X", "Starfield", "starfield", "starfield_p",
                        GameDescriptions.get("Starfield"), "RPG", "Fantasy", "Action", false),
        };
    }
}