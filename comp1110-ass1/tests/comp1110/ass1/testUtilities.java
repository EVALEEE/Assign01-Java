package comp1110.ass1;

public class testUtilities {

    static void initialiseTiles(String tiles, ColourCatch game) {
        String[] split = tiles.split("(?<=\\G.{4})");
        for (String s : split) {
            if (!s.isEmpty()) {
                Tile t = new Tile(s);
                Tile tile = getTile(t, game);
                assert tile != null;
                game.placeTile(tile);
            }
        }
    }

    static Tile getTile(Tile t, ColourCatch game) {
        Tile[] gameTiles = game.tiles;
        for (Tile tile : gameTiles)
            if (tile.getID() == t.getID()) {
                tile.setLocation(t.getLocation());
                tile.setOrientation(t.getOrientation());
                return tile;
            }
        return null;
    }

    public static final String[] SOLUTIONS = {
            "An11Bn22Cn10Ds02Ew00", // 1
            "Aw00Bn22Cw20Ds02Ee10", // 2
            "As00Bn11Cn12De20Ew01", // 3
            "As01Bn02Cw20Dn00En12", // 4
            "Ae01Bn00Cw10Dn12Ee20", // 5
            "As11Bn22Cn02Dw00En10", // 6
            "Aw10Bn02Ce00Dw20En12", // 7
            "Ae20Bn00Cn12De01Ew10", // 8
            "An10Bn02Ce11De21Ee00", // 9
            "As10Bn02Cn12De00Es11", // 10
            "Aw20Bn22Cn00Dn02Es01", // 11
            "As10Bn22Cn11Dn02Ew00", // 12
            "As10Bn02Cn11Dw00En12", // 13
            "Ae00Bn02Cw10Ds12Ee20", // 14
            "As01Bn22Cw20Ds02En00", // 15
            "An10Bn00Cn12De01Es11", // 16
            "As10Bn02Ce21Dw00Ew11", // 17
            "As12Bn00Cw10De01Ee20", // 18
            "Aw00Bn02Cs11Dn10En12", // 19
            "As02Bn20Cs00Dw21En01", // 20
            "Aw00Bn02Cs12Dn11Es10", // 21
            "Aw00Bn02Cn10Dn11En12", // 22
            "Ae21Bn02Cs10Dw00Ew11", // 23
            "Ae00Bn02Cn12Dw20Ew10", // 24
            "Ae20Bn02Ce10Ds12Ee00", // 25
            "As11Bn22Cs10Ds02Ew00", // 26
            "An02Bn22Cn11Ds10Ew00", // 27
            "Ae21Bn00Cw11Ds10Ew01", // 28
            "Aw10Bn02Cs12Dw20Ee00", // 29
            "Ae10Bn22Cn02Dw20Ew00", // 30
            "Aw20Bn22Ce10Dn02Ew00", // 31
            "As02Bn20Cw00De21Ee10", // 32
            "As11Bn00Cn12Dn10Ee01", // 33
            "As11Bn22Cn02Dn10Ee00", // 34
            "Ae01Bn00Cw11Ds10Ee21", // 35
            "Aw20Bn02Cs01Dn00En12", // 36
            "Ae21Bn00Cn02Dn10Es01", // 37
            "Aw10Bn20Ce00Dw21En02", // 38
            "Aw00Bn22Ce10Dn02Ee20", // 39
            "As11Bn22Ce00Dn02Es10", // 40
            "As00Bn11Cn12De01Ew20", // 41
            "Ae00Bn22Cn02Dw10Ee20", // 42
            "An10Bn02Ce11Dw00Ew21", // 43
            "Ae01Bn20Ce11Ds00Ew21", // 44
            "Aw11Bn00Ce21Ds10Ew01", // 45
            "An00Bn20Ce01De11Ew21", // 46
            "Aw20Bn00Cn12Dw10Ew01", // 47
            "As10Bn22Cw00Dn02En11", // 48
            "An02Bn20Cs01De21Es00", // 49
            "An12Bn00Ce20De01Ew10", // 50
            "An00Bn02Ce20Ds01En12", // 51
            "Aw01Bn22Cs00Dw20Ew11", // 52
            "As00Bn11Ce20Dn12Ew01", // 53
            "As10Bn00Cn11Ds12Ew01", // 54
            "Aw00Bn22Cw20Dw10En02", // 55
            "As10Bn00Cn11De01En12", // 56
            "An10Bn22Ce00Ds11Es02", // 57
            "An02Bn22Ce10Dw00Ee20", // 58
            "Ae10Bn00Ce20Dn12Ew01", // 59
            "As10Bn00Cn01Ds02Ee21", // 60
    };


}
