package test;

import main.domeinLaag.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class VluchtTest {

    static LuchtvaartMaatschappij lvm;
    static Fabrikant f1;
    static VliegtuigType vtt1;
    static Vliegtuig vt1;
    static Luchthaven lh1, lh2;
    static Vlucht vl1, vl2;

    @BeforeEach
    public void initialize() {
        try {
            lvm = new LuchtvaartMaatschappij("NLM");
            f1 = new Fabrikant("Airbus", "G. Dejenelle");
            vtt1 = f1.creeervliegtuigtype("A-200", 140);
            Calendar datum = Calendar.getInstance();
            datum.set(2000, 01, 01);
            vt1 = new Vliegtuig(lvm, vtt1, "Luchtbus 100", datum);
            Land l1 = new Land("Nederland", 31);
            Land l2 = new Land("België", 32);
            lh1 = new Luchthaven("Schiphol", "ASD", true, l1);
            lh2 = new Luchthaven("Tegel", "TEG", true, l2);
            Calendar vertr = Calendar.getInstance();
            vertr.set(2020, 03, 30, 14, 15, 0);
            Calendar aank = Calendar.getInstance();
            aank.set(2020, 03, 30, 15, 15, 0);
            vl1 = new Vlucht(vt1, lh1, lh2, vertr, aank);
            vertr.set(2020, 4, 1, 8, 15, 0);
            aank.set(2020, 4, 1, 9, 15, 0);
            vl2 = new Vlucht(vt1, lh1, lh2, vertr, aank);
        } catch (Exception e) {
            String errorMessage = "Exception: " + e.getMessage();
            System.out.println(errorMessage);
        }
    }

    /**
     * Business rule:
     * De bestemming moet verschillen van het vertrekpunt van de vlucht.
     */

    @Test
    public void testBestemmingMagNietGelijkZijnAanVertrek_False() {
        Vlucht vlucht = new Vlucht();
        try {
            vlucht.zetVliegtuig(vt1);
            vlucht.zetVertrekpunt(lh1);
            Luchthaven bestemming = vlucht.getBestemming();
            assertNull(bestemming);
            vlucht.zetBestemming(lh1);
            // De test zou niet verder mogen komen: er moet al een exception gethrowd zijn.
            bestemming = vlucht.getBestemming();
            assertNotEquals(bestemming, lh1);
        } catch (IllegalArgumentException e) {
            Luchthaven bestemming = vlucht.getBestemming();
            assertNotEquals(bestemming, lh1);
        }
    }

    @Test
    public void testBestemmingMagNietGelijkZijnAanVertrek_True() {
        Vlucht vlucht = new Vlucht();
        Luchthaven bestemming;
        try {
            vlucht.zetVliegtuig(vt1);
            vlucht.zetVertrekpunt(lh2);
            bestemming = vlucht.getBestemming();
            assertNull(bestemming);
            vlucht.zetBestemming(lh1);
            bestemming = vlucht.getBestemming();
            assertEquals(bestemming, lh1);
        } catch (IllegalArgumentException e) {
            bestemming = vlucht.getBestemming();
            assertEquals(bestemming, lh1);
        }
    }


    /**
     * Business rule: vertrektijd mag niet gelijk zijn aan de aankomsttijd
     * xxx
     */

    @Test
    public void testVertrekTijdMagNietGelijkZijnAanDeAankomstTijd_True() {
        Calendar testVertrektijd1 = vl1.getVertrekTijd();
        Calendar testAankomstTijd1 = vl1.getAankomstTijd();
        assertNotEquals(testVertrektijd1, testAankomstTijd1);
    }


    /**
     * Business rule: vertrektijd mag niet groter zijn dan de aankomsttijd
     * xxx
     */

    @Test
    public void testVertrekTijdMagNietGroterZijnDanDeAankomstTijd_True() {
        Calendar testVertrektijd = vl1.getVertrekTijd();
        Calendar testAankomstTijd = vl1.getAankomstTijd();

        int verschilInTijd = testVertrektijd.compareTo(testAankomstTijd);
        assertFalse(verschilInTijd > 0, "vertrektijd mag niet groter zijn dan aankomst tijd");
    }

    @Test
    public void testVertektijdOngeldigeDatumTijdThrowsVluchtException() {
        Calendar vertrektijd = Calendar.getInstance();
        vertrektijd.set(2025, 9, 31, 24, 0, 0);

        Vlucht vlucht = new Vlucht();
        assertThrows(VluchtException.class,
                () -> {
                    vlucht.zetVertrekTijd(vertrektijd);
                });

        try {
            vlucht.zetVertrekTijd(vertrektijd);
        } catch (VluchtException e) {
            assertEquals("Geen geldig tijdstip!", e.getMessage());
        }
    }

    @Test
    public void testVertektijdEnAankomstijdOngeldigeDatumTijdThrowsVluchtException() {
        Calendar vertrektijd = Calendar.getInstance();
        Calendar aankomstTijd = Calendar.getInstance();
        vertrektijd.set(2025, 9, 31, 24, 0, 0);
        aankomstTijd.set(2025, 9, 31, 24, 0, 1);


        Vlucht vlucht = new Vlucht();

        assertThrows(VluchtException.class,
                () -> {
                    vlucht.zetVertrekTijd(vertrektijd);
                    vlucht.zetAankomstTijd(aankomstTijd);
                });

        try {
            vlucht.zetAankomstTijd(vertrektijd);
        } catch (VluchtException e) {
            assertEquals("Geen geldig tijdstip!", e.getMessage());
        }

    }
	/**
	 * Business rule: aankomsttijd mag niet voor vertrektijd zijn.
	 *
	 */
	@Test
	public void testAankomstTijdMagVoorVertrektijd_ThrowsVluchtException(){

		Vlucht testVlucht = new Vlucht();
		Calendar testVertrekTijdNu = Calendar.getInstance();
		Calendar testBestemmingTijdNu = Calendar.getInstance();

		testVertrekTijdNu.add(Calendar.MINUTE,1);


		testVlucht.zetVliegtuig(vt1);
		testVlucht.zetVertrekpunt(lh2);
		testVlucht.zetBestemming(lh1);
		assertThrows(VluchtException.class,() ->{

		testVlucht.zetVertrekTijd(testVertrekTijdNu);
		testVlucht.zetAankomstTijd(testBestemmingTijdNu);});

		try {
			testVlucht.zetVertrekTijd(testVertrekTijdNu);
			testVlucht.zetAankomstTijd(testBestemmingTijdNu);
		}catch (VluchtException e) {
			assertEquals("Aankomsttijd voor vertrektijd", e.getMessage());
		}
	}

	@Test
	public void testAankomstTijdMagNietVoorVertrektijd_ThrowsGeenVluchtException(){

			Vlucht testVlucht2 = new Vlucht();
			Calendar testVertrekTijdNu = Calendar.getInstance();
			Calendar testBestemmingTijdNu = Calendar.getInstance();

			testBestemmingTijdNu.add(Calendar.MINUTE, 1);

			testVlucht2.zetVliegtuig(vt1);
			testVlucht2.zetVertrekpunt(lh2);
			testVlucht2.zetBestemming(lh1);
		try {
			testVlucht2.zetVertrekTijd(testVertrekTijdNu);
			testVlucht2.zetAankomstTijd(testBestemmingTijdNu);
		}catch (VluchtException e){
			assertEquals("", e.getMessage());
		}
	}
}
