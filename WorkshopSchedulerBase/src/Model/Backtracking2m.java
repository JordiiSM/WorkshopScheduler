package Model;

import view.ScheduleView;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class Backtracking2m {

    private static Workshops workshops;
    private static int[][] calendario;
    private static int cont;
    private static int maxcont;
    private int[] solucion;
    private LocalDateTime fechaInicial;
    private Instant inicio;
    private int contadorSoluciones;

    public int[] getSolucion() {
        return solucion;
    }


    public Backtracking2m(Workshops workshops, LocalDateTime fechaInicial, Instant inicio) {
        this.workshops = workshops;
        calendario = new int[12][5];
        cont = 0;
        maxcont = 0;
        this.fechaInicial = fechaInicial;
        solucion = new int[workshops.getWorkshops().size()];
        this.inicio = inicio;
        contadorSoluciones = 0;

    }

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
            cont = cont + workshops.getWorkshops().get(k).getTimetable().size();    //contador horas
            for(int i = 0;i<workshops.getWorkshops().get(k).getTimetable().size();i++){
                calendario[workshops.getWorkshops().get(k).getTimetable().get(i).getHour()][workshops.getWorkshops().get(k).getTimetable().get(i).getDay()]++;
            }
        }
    }
    public static void desmarcar(int[] x,int k){
        if(x[k]==1){
            cont = cont - workshops.getWorkshops().get(k).getTimetable().size();
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
        if(maxcont==cont){
            contadorSoluciones++;
        }
        if (maxcont<cont){
            contadorSoluciones = 1;
            maxcont = cont;
            for (int i = 0;i<x.length;i++){
                solucion[i]=x[i];
            }
        }
    }
    public void verSolución(int[] x){
        int horastotales = 0;
        int talleresTotales = 0;
        final ScheduleView view = new ScheduleView();
        view.resetAllContents();
        SwingUtilities.invokeLater(() -> view.setVisible(true));
        //System.out.println("Solucion "+contadorSoluciones);
        float price;
        for (int i = 0;i<x.length;i++){
            if(x[i]==1){
                talleresTotales++;
                price = workshops.getWorkshops().get(i).getPrice();
                horastotales += workshops.getWorkshops().get(i).getTimetable().size();
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
        view.setStartDateContent(fechaInicial);
        LocalDateTime fechaFinal = LocalDateTime.now();
        view.setFinishDateContent(fechaFinal);
        Duration duration;
        Instant fin = Instant.now();
        System.out.println(Duration.between(inicio,fin));
        view.setDurationContent(Duration.between(inicio,fin));
        view.setTotalWorkshopsContent(talleresTotales);
        view.setTotalHoursContent(horastotales);
        view.setSolutionsContent(contadorSoluciones);

    }


}
