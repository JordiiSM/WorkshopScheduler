package Model;

import view.ScheduleView;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class Backtracking3m {

    private static Workshops workshops;
    private float presupuesto;
    private int[] xmejor;
    private static int[] cats;
    private static int contcats;
    private static BigDecimal precio;
    private int[] solucion;
    private LocalDateTime fechaInicial;
    private Instant inicio;
    private int discount;
    private BigDecimal maxprecio;
    private static int[][] calendario;
    private static int bestcontcats;
    private int contadorSoluciones;



    public Backtracking3m(Workshops workshops, float presupuesto, LocalDateTime fechaInicial, Instant inicio) {
        this.workshops = workshops;
        this.presupuesto = presupuesto;
        calendario = new int[12][5];
        xmejor = new int [workshops.getWorkshops().size()];
        cats = new int[5];
        contcats = 0;
        maxprecio = BigDecimal.ZERO;
        precio = BigDecimal.ZERO;
        this.fechaInicial = fechaInicial;
        solucion = new int[workshops.getWorkshops().size()];
        this.inicio = inicio;
        discount = 0;
        bestcontcats = 0;
        contadorSoluciones = 0;
    }

    public int[] getSolucion() {
        return solucion;
    }

    public void setSolucion(int[] solucion) {
        this.solucion = solucion;
    }

    //    public static Workshops workshops = new Workshops();
    public static boolean buena(int[] x, int k){
        if(x[k]==1){

            for(int i = 0; i<workshops.getWorkshops().get(k).getTimetable().size();i++){
                if(calendario[workshops.getWorkshops().get(k).getTimetable().get(i).getHour()][workshops.getWorkshops().get(k).getTimetable().get(i).getDay()] > 1) {
                    return false;
                }
            }
        //compatibilidad (matriz)
            for (int i = 0;i<k;i++){ //recorre todos los talleres hasta el k

                if(x[i]==1){

                    if(workshops.getCompatibilityMatrix().get(i).get(k)==0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void marcar(int[] x, int k){
        if(x[k]==1){
            precio = precio.add(new BigDecimal(Float.toString(workshops.getWorkshops().get(k).getPrice())));
            cats[workshops.getWorkshops().get(k).getCategory()-1]++;
            if(cats[workshops.getWorkshops().get(k).getCategory()-1]==1){
                contcats++;
            }
            for(int i = 0;i<workshops.getWorkshops().get(k).getTimetable().size();i++){
                calendario[workshops.getWorkshops().get(k).getTimetable().get(i).getHour()][workshops.getWorkshops().get(k).getTimetable().get(i).getDay()]++;
            }
        }
    }
    public static void desmarcar(int[] x,int k){
        if(x[k]==1){
            precio = precio.subtract(new BigDecimal(Float.toString(workshops.getWorkshops().get(k).getPrice())));
            cats[workshops.getWorkshops().get(k).getCategory()-1]--;
            if(cats[workshops.getWorkshops().get(k).getCategory()-1]==0){
                contcats--;
            }
            for(int i = 0;i<workshops.getWorkshops().get(k).getTimetable().size();i++){
                calendario[workshops.getWorkshops().get(k).getTimetable().get(i).getHour()][workshops.getWorkshops().get(k).getTimetable().get(i).getDay()]--;
            }
        }

    }
    public void Backtracking(int[] x, int k){

        x[k]  = -1;  //PreparaRecorridoNivel

        while (x[k] < 1){
            x[k]++;  //siguienteHermano
            marcar(x,k); //marcar
            if (k == workshops.getWorkshops().size()-1){ //solucion
                if (buena(x,k)){ //factible
                    tratarSolucion(x);
                }
            }
            if (k < workshops.getWorkshops().size()-1){ //no solucion
                if (buena(x,k)){ //completable
                    Backtracking(x,k+1);
                }
            }

            desmarcar(x,k);
        }

    }
    public void tratarSolucion(int[] x){

        BigDecimal preciodiscount = BigDecimal.ZERO;
        if(contcats ==2) preciodiscount = precio.multiply(BigDecimal.valueOf(0.95));
        if(contcats>2) preciodiscount = precio.multiply(BigDecimal.valueOf(0.85));
        if((preciodiscount.compareTo(new BigDecimal(Float.toString(presupuesto))) <= 0) && (preciodiscount.compareTo(maxprecio) == 0)){
            contadorSoluciones++;
        }
        if((preciodiscount.compareTo(new BigDecimal(Float.toString(presupuesto))) <= 0) && (preciodiscount.compareTo(maxprecio) == 1)){
            contadorSoluciones = 1;
            bestcontcats = contcats;
            maxprecio = preciodiscount;
            xmejor = x;
            for (int i = 0;i<x.length;i++){
                solucion[i]=x[i];
            }
        }
    }
    public void verSolución(int[] x){
        Instant fin = Instant.now();
        LocalDateTime fechaFinal = LocalDateTime.now();
        int talleresTotales = 0;
        int horasTotales = 0;
        float preciobase = 0;
        final ScheduleView view = new ScheduleView();
        view.resetAllContents();
        SwingUtilities.invokeLater(() -> view.setVisible(true));
        //System.out.println("Solucion "+contadorSoluciones);
        float price;
        int categorias[] = new int[5];
        for (int i = 0;i<x.length;i++){
            if(x[i]==1){
                preciobase += workshops.getWorkshops().get(i).getPrice();
                categorias[workshops.getWorkshops().get(i).getCategory()-1]++;
                horasTotales = horasTotales+workshops.getWorkshops().get(i).getTimetable().size();
                talleresTotales++;
                price = workshops.getWorkshops().get(i).getPrice();
                for(int j = 0;j<workshops.getWorkshops().get(i).getTimetable().size();j++){
                    view.setCellContent(workshops.getWorkshops().get(i).getAcronym(),
                            workshops.getWorkshops().get(i).getCategory(),
                            (float)price,
                            new Color(workshops.getWorkshops().get(i).getRgbColor().get(0),workshops.getWorkshops().get(i).getRgbColor().get(1),workshops.getWorkshops().get(i).getRgbColor().get(2)),
                            //(Color) workshops.getWorkshops().get(i).getRgbColor(),
                            workshops.getWorkshops().get(i).getTimetable().get(j).getHour(),
                            workshops.getWorkshops().get(i).getTimetable().get(j).getDay()
                    );
                }
//                System.out.println("Se visita taller "+workshops.getWorkshops().get(i).getAcronym());

            }
        }
        view.setSolutionsContent(contadorSoluciones);
        view.setStartDateContent(fechaInicial);

        view.setFinishDateContent(fechaFinal);
        Duration duration;

//        System.out.println(Duration.between(inicio,fin));
        view.setDurationContent(Duration.between(inicio,fin));
        view.setTotalWorkshopsContent(talleresTotales);
        view.setTotalHoursContent(horasTotales);
        view.setLimitCostContent((float) presupuesto);
        view.setBaseCostContent((float) preciobase);
        int contcats = 0;
        for (int i = 0;i<=4;i++){
            if(categorias[i]>0){
                contcats++;
            }
        }
        if(contcats ==2) discount = 5;
        if(contcats>2) discount = 15;

        view.setDiscountContent(discount);
        view.setFinalCostContent(maxprecio.floatValue());
        view.setCategoryContent(1,categorias[0]);
        view.setCategoryContent(2,categorias[1]);
        view.setCategoryContent(3,categorias[2]);
        view.setCategoryContent(4,categorias[3]);
        view.setCategoryContent(5,categorias[4]);

    }
}
