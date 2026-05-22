package lab.core.session;

import lab.features.researchers.models.ResearcherModel;

public final class UserSession {
    private static ResearcherModel currentResearcher;

    private UserSession() {
    }

    public static void login(ResearcherModel researcher) {
        currentResearcher = researcher;
    }

    public static void logout() {
        currentResearcher = null;
    }

    public static ResearcherModel getCurrentResearcher() {
        return currentResearcher;
    }

    public static int getCurrentResearcherId() {
        if (currentResearcher == null) {
            throw new IllegalStateException("Nema ulogovanog istrazivaca.");
        }
        return currentResearcher.getIdIstrazivac();
    }

    public static String getDisplayName() {
        if (currentResearcher == null) {
            return "Nema ulogovanog istrazivaca";
        }

        return currentResearcher.getIme()
                + " "
                + currentResearcher.getPrezime()
                + " ("
                + currentResearcher.getKorisnickoIme()
                + ")";
    }
}
