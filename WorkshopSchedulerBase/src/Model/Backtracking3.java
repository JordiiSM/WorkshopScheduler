package Model;

import view.ScheduleView;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class Backtracking3 {

    private static Workshops workshops;
    private float presupuesto;
    private int[] xmejor;
    private BigDecimal maxprecio;
    private int[] solucion;
    private LocalDateTime fechaInicial;
    private Instant inicio;
    private int cats[];
    private int discount;
    private int contadorSoluciones;


    public int[] getSolucion() {
        return solucion;
    }

    public Backtracking3(Workshops workshops, float presupuesto, LocalDateTime fechaInicial, Instant inicio) {
        this.workshops = workshops;
        this.presupuesto = presupuesto;
        xmejor = new int [workshops.getWorkshops().size()];
        maxprecio = BigDecimal.ZERO;
        this.fechaInicial = fechaInicial;
        solucion = new int[workshops.getWorkshops().size()];
        this.inicio = inicio;
        cats = new int[5];
        discount = 0;
        contadorSoluciones = 0;


    }

    //    public static Workshops workshops = new Workshops();
    public static boolean buena(int[] x, int k){

        if(x[k]==1){

            for (int i = 0;i<k;i++){ //recorre todos los talleres hasta el k

                if(x[i]==1){

                    for (int j = 0;j<workshops.getWorkshops().get(i).getTimetable().size();j++){    //recorre los horarios del taller i
                        //System.out.println(workshops.getWorkshops().get(i).getTimetable().size()+"Num horarios del workshop"+ i);
                        for (int a = 0;a<workshops.getWorkshops().get(k).getTimetable().size();a++){ //recorre los horarios del taller k

                            if(workshops.getWorkshops().get(i).getTimetable().get(j).getHour()==workshops.getWorkshops().get(k).getTimetable().get(a).getHour() &&
                                    workshops.getWorkshops().get(i).getTimetable().get(j).getDay()==workshops.getWorkshops().get(k).getTimetable().get(a).getDay()){
                                return false;
                            }
                        }

                    }
                    if(workshops.getCompatibilityMatrix().get(i).get(k)==0){
                        return false;
                    }
                }
            }
        }
        return true;

    }


    public void Backtracking(int[] x, int k){
        x[k]  = -1;  //PreparaRecorridoNivel
        while (x[k] < 1){
            x[k]++; //siguienteHermano
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
        }
    }

    public void tratarSolucion(int[] x){
//        System.out.println("Solucion "+contadorSoluciones);
        BigDecimal precio = new BigDecimal(Float.toString(0));
        precio = precio.setScale(2, RoundingMode.HALF_UP);
        int[] categorias = new int[5];
        int contcats = 0;
        for (int i = 0;i<x.length;i++){
            if(x[i]==1){
                categorias[workshops.getWorkshops().get(i).getCategory()-1]++;
                precio = precio.add(new BigDecimal(Float.toString(workshops.getWorkshops().get(i).getPrice())));
                cats[workshops.getWorkshops().get(i).getCategory()-1]++;
            }
        }

        for (int i = 0;i<=4;i++){
            if(categorias[i]>0){
                contcats++;
            }
        }

        if(contcats ==2){
            precio = precio.multiply(new BigDecimal("0.95"));
            discount = 5;
        }
        if(contcats>2){
            precio = precio.multiply(new BigDecimal("0.85"));
            discount = 15;
        }
        contcats = 0;
        System.out.println("Precio Solucion = "+precio);
        if((precio.compareTo(new BigDecimal(Float.toString(presupuesto))) <= 0) && (precio.compareTo(maxprecio) == 0)){
            contadorSoluciones++;
        }
        if((precio.compareTo(new BigDecimal(Float.toString(presupuesto))) <= 0) && (precio.compareTo(maxprecio) == 1)){
            contadorSoluciones = 1;
            maxprecio = precio;
            xmejor = x;
            System.out.println("Precio total = "+precio);
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
                System.out.println("Se visita taller "+workshops.getWorkshops().get(i).getAcronym());

            }
        }
        view.setSolutionsContent(contadorSoluciones);
        view.setStartDateContent(fechaInicial);

        view.setFinishDateContent(fechaFinal);
        Duration duration;

        System.out.println(Duration.between(inicio,fin));
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
