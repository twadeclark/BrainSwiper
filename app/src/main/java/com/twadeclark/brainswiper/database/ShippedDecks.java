package com.twadeclark.brainswiper.database;

import java.util.ArrayList;

public class ShippedDecks {
    public static ArrayList<Deck> getDecks() {
        ArrayList<Deck> deckList = new ArrayList<>();

        for (String[] s :shippedDecks) {
            String deckName = s[0];
            String deckContents = s[1];

            Deck newDeck = new Deck(deckName, deckContents);
            deckList.add(newDeck);
        }

        return deckList;
    }

    private static final String[][] shippedDecks = {
            {"State Capitals",
                    "Alabama, Montgomery\n" +
                    "Alaska, Juneau\n" +
                    "Arizona, Phoenix\n" +
                    "Arkansas, Little Rock\n" +
                    "California, Sacramento\n" +
                    "Colorado, Denver\n" +
                    "Connecticut, Hartford\n" +
                    "Delaware, Dover\n" +
                    "Florida, Tallahassee\n" +
                    "Georgia, Atlanta\n" +
                    "Hawaii, Honolulu\n" +
                    "Idaho, Boise\n" +
                    "Illinois, Springfield\n" +
                    "Indiana, Indianapolis\n" +
                    "Iowa, Des Moines\n" +
                    "Kansas, Topeka\n" +
                    "Kentucky, Frankfort\n" +
                    "Louisiana, Baton Rouge\n" +
                    "Maine, Augusta\n" +
                    "Maryland, Annapolis\n" +
                    "Massachusetts, Boston\n" +
                    "Michigan, Lansing\n" +
                    "Minnesota, Saint Paul\n" +
                    "Mississippi, Jackson\n" +
                    "Missouri, Jefferson City\n" +
                    "Montana, Helena\n" +
                    "Nebraska, Lincoln\n" +
                    "Nevada, Carson City\n" +
                    "New Hampshire, Concord\n" +
                    "New Jersey, Trenton\n" +
                    "New Mexico, Santa Fe\n" +
                    "New York, Albany\n" +
                    "North Carolina, Raleigh\n" +
                    "North Dakota, Bismarck\n" +
                    "Ohio, Columbus\n" +
                    "Oklahoma, Oklahoma City\n" +
                    "Oregon, Salem\n" +
                    "Pennsylvania, Harrisburg\n" +
                    "Rhode Island, Providence\n" +
                    "South Carolina, Columbia\n" +
                    "South Dakota, Pierre\n" +
                    "Tennessee, Nashville\n" +
                    "Texas, Austin\n" +
                    "Utah, Salt Lake City\n" +
                    "Vermont, Montpelier\n" +
                    "Virginia, Richmond\n" +
                    "Washington, Olympia\n" +
                    "West Virginia, Charleston\n" +
                    "Wisconsin, Madison\n" +
                    "Wyoming, Cheyenne"},

            {"Spanish Numbers",
                    "1, uno\n" +
                    "2, dos\n" +
                    "3, tres\n" +
                    "4, cuatro\n" +
                    "5, cinco\n" +
                    "6, seis\n" +
                    "7, siete\n" +
                    "8, ocho\n" +
                    "9, nueve\n" +
                    "10, diez\n" +
                    "11, once\n" +
                    "12, doce\n" +
                    "13, trece\n" +
                    "14, catorce\n" +
                    "15, quince\n" +
                    "16, dieciséis\n" +
                    "17, diecisiete\n" +
                    "18, dieciocho\n" +
                    "19, diecinueve\n" +
                    "20, veinte\n" +
                    "30, treinta\n" +
                    "40, cuarenta\n" +
                    "50, cincuenta\n" +
                    "60, sesenta\n" +
                    "70, setenta\n" +
                    "80, ochenta\n" +
                    "90, noventa\n" +
                    "100, cien\n"},

            {"French Numbers", "0, zéro\n" +
                    "1, un\n" +
                    "2, deux\n" +
                    "3, trois\n" +
                    "4, quatre\n" +
                    "5, cinq\n" +
                    "6, six\n" +
                    "7, sept\n" +
                    "8, huit\n" +
                    "9, neuf\n" +
                    "10, dix\n" +
                    "11, onze\n" +
                    "12, douze\n" +
                    "13, treize\n" +
                    "14, quatorze\n" +
                    "15, quinze\n" +
                    "16, seize\n" +
                    "17, dix-sept\n" +
                    "18, dix-huit\n" +
                    "19, dix-neuf\n" +
                    "20, vingt\n" +
                    "30, trente\n" +
                    "40, quarante\n" +
                    "50, cinquante\n" +
                    "60, soixante\n" +
                    "70, soixante-dix\n" +
                    "80, quatre-vingt\n" +
                    "90, quatre-vingt-dix\n" +
                    "100, cent"},

    };


}
