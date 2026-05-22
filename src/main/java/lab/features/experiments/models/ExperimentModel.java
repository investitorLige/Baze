package lab.features.experiments.models;

public class ExperimentModel {
    private final int idIzvodjenje;
    private final String nazivEksperimenta;
    private final String teorija;
    private final String laboratorija;
    private final String datum;
    private final String status;

    public ExperimentModel(
            int idIzvodjenje,
            String nazivEksperimenta,
            String teorija,
            String laboratorija,
            String datum,
            String status
    ) {
        this.idIzvodjenje = idIzvodjenje;
        this.nazivEksperimenta = nazivEksperimenta;
        this.teorija = teorija;
        this.laboratorija = laboratorija;
        this.datum = datum;
        this.status = status;
    }

    public int getIdIzvodjenje() {
        return idIzvodjenje;
    }

    public String getNazivEksperimenta() {
        return nazivEksperimenta;
    }

    public String getTeorija() {
        return teorija;
    }

    public String getLaboratorija() {
        return laboratorija;
    }

    public String getDatum() {
        return datum;
    }

    public String getStatus() {
        return status;
    }
}
