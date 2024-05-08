package Helpers;

public class Film {

	private int id,film_suresi;
	double film_puani;
	String film_adi, yonetmen, tur, aciklama,ulke,oyuncular,vizyon_tarihi,film_gorselleri;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFilm_suresi() {
		return film_suresi;
	}
	public void setFilm_suresi(int film_suresi) {
		this.film_suresi = film_suresi;
	}
	public double getFilm_puani() {
		return film_puani;
	}
	public void setFilm_puani(double film_puani) {
		this.film_puani = film_puani;
	}
	public String getFilm_adi() {
		return film_adi;
	}
	public void setFilm_adi(String film_adi) {
		this.film_adi = film_adi;
	}
	public String getYonetmen() {
		return yonetmen;
	}
	public void setYonetmen(String yonetmen) {
		this.yonetmen = yonetmen;
	}
	public String getTur() {
		return tur;
	}
	public void setTur(String tur) {
		this.tur = tur;
	}
	public String getAciklama() {
		return aciklama;
	}
	public void setAciklama(String aciklama) {
		this.aciklama = aciklama;
	}
	public String getUlke() {
		return ulke;
	}
	public void setUlke(String ulke) {
		this.ulke = ulke;
	}
	public String getOyuncular() {
		return oyuncular;
	}
	public void setOyuncular(String oyuncular) {
		this.oyuncular = oyuncular;
	}
	public String getVizyon_tarihi() {
		return vizyon_tarihi;
	}
	public void setVizyon_tarihi(String vizyon_tarihi) {
		this.vizyon_tarihi = vizyon_tarihi;
	}
	public String getFilm_gorselleri() {
		return film_gorselleri;
	}
	public void setFilm_gorselleri(String film_gorselleri) {
		this.film_gorselleri = film_gorselleri;
	}
}