package Model;

import view.ScheduleView;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class Backtracking1 {

    private static Workshops workshops;
    private static int contadorSoluciones;
    private int[] solucion;
    private LocalDateTime fechaInicial;
    private Instant inicio;
    public Backtracking1(Workshops workshops,LocalDateTime fechaInicial,Instant inicio) {

        this.workshops = workshops;
       this.fechaInicial = fechaInicial;
       solucion = new int[workshops.getWorkshops().size()];
       this.inicio = inicio;

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
        while (x[k] < 1){//hay sucesor
            x[k]++; //siguienteHermano
            if (k == workshops.getWorkshops().size()-1){ //solucion
                if (buena(x,k)){ //factible
                    this.contadorSoluciones++;
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

        for (int i = 0;i<x.length;i++){
            solucion[i]=x[i];
        }

    }

    public void verSolución(int[] x){
        Instant fin = Instant.now();
        LocalDateTime fechaFinal = LocalDateTime.now();
        final ScheduleView view = new ScheduleView();
        view.resetAllContents();
        SwingUtilities.invokeLater(() -> view.setVisible(true));

        //System.out.println("Solucion "+contadorSoluciones);
        float price;
        for (int i = 0;i<x.length;i++){
            if(x[i]==1){
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
    }
}
