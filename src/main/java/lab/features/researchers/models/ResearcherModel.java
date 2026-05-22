package lab.features.researchers.models;

public class ResearcherModel {
    private final int idIstrazivac;
    private final String ime;
    private final String prezime;
    private final String datumRodjenja;
    private final String email;
    private final String tel;
    private final String korisnickoIme;
    private final String titula;
    private final String specijalizacija;
    private final Integer godineIskustva;
    private final String institucija;

    public ResearcherModel(
            int idIstrazivac,
            String ime,
            String prezime,
            String datumRodjenja,
            String email,
            String tel,
            String korisnickoIme,
            String titula,
            String specijalizacija,
            Integer godineIskustva,
            String institucija
    ) {
        this.idIstrazivac = idIstrazivac;
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.email = email;
        this.tel = tel;
        this.korisnickoIme = korisnickoIme;
        this.titula = titula;
        this.specijalizacija = specijalizacija;
        this.godineIskustva = godineIskustva;
        this.institucija = institucija;
    }

    public int getIdIstrazivac() {
        return idIstrazivac;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getDatumRodjenja() {
        return datumRodjenja;
    }

    public String getEmail() {
        return email;
    }

    public String getTel() {
        return tel;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public String getTitula() {
        return titula;
    }

    public String getSpecijalizacija() {
        return specijalizacija;
    }

    public Integer getGodineIskustva() {
        return godineIskustva;
    }

    public String getInstitucija() {
        return institucija;
    }
}
