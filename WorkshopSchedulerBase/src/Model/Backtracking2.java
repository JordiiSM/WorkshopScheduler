package Model;

import view.ScheduleView;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class Backtracking2 {

    private static Workshops workshops;
    private int cont;
    private int[] xmejor;
    private int[] solucion;
    private LocalDateTime fechaInicial;
    private Instant inicio;
    private int contadorSoluciones;

    public int[] getSolucion() {
        return solucion;
    }

    public int[] getXmejor() {
        return xmejor;

    }



    public Backtracking2(Workshops workshops, LocalDateTime fechaInicial, Instant inicio) {

        this.workshops = workshops;
        cont = 0;
        xmejor = new int [workshops.getWorkshops().size()];
        this.fechaInicial = fechaInicial;
        solucion = new int[workshops.getWorkshops().size()];
        this.inicio = inicio;
        contadorSoluciones = 0;

    }

    //    public static Workshops workshops = new Workshops();
    public static boolean buena(int[] x, int k){

        if(x[k]==1){

            for (int i = 0;i<k;i++){ //recorre todos los talleres hasta el k

                if(x[i]==1){

                    for (int j = 0;j<workshops.getWorkshops().get(i).getTimetable().size();j++){    //recorre los horarios del taller i

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
        int contador = 0;
        for (int i = 0;i<x.length;i++){
            if(x[i]==1){
                contador = contador+workshops.getWorkshops().get(i).getTimetable().size();
            }
        }
        if(contador==cont){
            contadorSoluciones++;
        }
        if (contador>cont){
            contadorSoluciones=1;
            cont = contador;
            xmejor = x;
            System.out.println("La mejor opcion es ir a los talleres: ");
            for (int i = 0;i<x.length;i++){
                solucion[i]=x[i];
            }
        }
    }
    public void verSolución(int[] x){
        int talleresTotales = 0;
        final ScheduleView view = new ScheduleView();
        view.resetAllContents();
        SwingUtilities.invokeLater(() -> view.setVisible(true));
        //System.out.println("Solucion "+contadorSoluciones);
        double price;
        for (int i = 0;i<x.length;i++){
            if(x[i]==1){
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
        view.setStartDateContent(fechaInicial);
        LocalDateTime fechaFinal = LocalDateTime.now();
        view.setFinishDateContent(fechaFinal);
        Duration duration;
        Instant fin = Instant.now();
        System.out.println(Duration.between(inicio,fin));
        view.setDurationContent(Duration.between(inicio,fin));
        view.setTotalWorkshopsContent(talleresTotales);
        view.setTotalHoursContent(cont);
        view.setSolutionsContent(contadorSoluciones);
    }
}
