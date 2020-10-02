import Model.*;
import com.google.gson.Gson;
import view.ScheduleView;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static Workshops workshops = new Workshops();
//    public static boolean buena(int[] x, int k, int[][] calendario){
//       for(int i = 0; i<workshops.getWorkshops().get(k).getTimetable().size();i++){
//           if(calendario[workshops.getWorkshops().get(k).getTimetable().get(i).getHour()][workshops.getWorkshops().get(k).getTimetable().get(i).getDay()] > 1) {
//                return false;
//           }
//       }
//       //falta comprobar compatibilidad (matriz)
//       return true;
//    }
//
//    public static int[][] marcar(int k, int[][] calendario){
//        for(int i = 0;i<workshops.getWorkshops().get(k).getTimetable().size();i++){
//            calendario[workshops.getWorkshops().get(k).getTimetable().get(i).getHour()][workshops.getWorkshops().get(k).getTimetable().get(i).getDay()]++;
//        }
//        return calendario;
//    }
//    public static int[][] desmarcar(int k, int[][] calendario){
//        for(int i = 0;i<workshops.getWorkshops().get(k).getTimetable().size();i++){
//            calendario[workshops.getWorkshops().get(k).getTimetable().get(i).getHour()][workshops.getWorkshops().get(k).getTimetable().get(i).getDay()]--;
//        }
//        return calendario;
//    }
//    public static void Backtracking(int[] x, int k,int[][] calendario){
//
//        x[k]  = -1;  //PreparaRecorridoNivel
//        calendario = marcar(k,calendario); //marcar
//        while (x[k] < 1){
//            x[k]++; //siguienteHermano
//
//            System.out.println(k+"<--- K");
//            if (k == workshops.getWorkshops().size()-1){ //solucion
//                if (buena(x,k,calendario)){ //factible
//                    System.out.println(workshops.getWorkshops().get(k).getAcronym()+"<---Compatible");
//                    //Tratar solucion
//                }
//            }
//            if (k < workshops.getWorkshops().size()-1){ //no solucion
//                if (buena(x,k,calendario)){ //completable
//                    System.out.println("Ha sido buena!!");
//                    Backtracking(x,k+1, calendario);
//                }else{
//                    System.out.println("No factible");
//                }
//            }
//
//            calendario = desmarcar(k,calendario);
//        }
//
//    }
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {


        int calendario [][] = new int[12][5];
        Gson gson = new Gson();

        int option;

        float presupuesto;
        Scanner sc = new Scanner(System.in);
        System.out.println("_-_-_- WorkshopScheduler -_-_-_");
        System.out.println("Introduce la ubicación del fichero:");
        String filename = sc.nextLine();
        workshops = gson.fromJson(new FileReader("src/"+filename), Workshops.class);
        int[] solucion = new int[workshops.getWorkshops().size()];
        int x[] = new int[workshops.getWorkshops().size()];
        System.out.println("Selecciona un objetivo\n" +
                " 1. Todas las configuraciones posibles.\n" +
                " 2. Maximizar horas.\n" +
                " 3. Maximizar presupuesto disponible.");
        option = sc.nextInt();
        System.out.println("¿Quieres aplicar mejoras en la eficiencia? (s/n)");
        char sn = sc.next().charAt(0);
        switch (option){
            case 1: if(sn=='s'){
                    LocalDateTime fechaInicial = LocalDateTime.now();
                    Instant inicio = Instant.now();
                    Backtracking1m b = new Backtracking1m(workshops,fechaInicial,inicio);
                    b.Backtracking(x,0);
                    solucion = b.getSolucion();
                    b.verSolución(solucion);
                }else if(sn =='n'){
                    LocalDateTime fechaInicial = LocalDateTime.now();
                    Instant inicio = Instant.now();
                    Backtracking1 b = new Backtracking1(workshops,fechaInicial,inicio);
                    b.Backtracking(x,0);
                    solucion = b.getSolucion();
                    b.verSolución(solucion);
                }
                break;
            case 2:if(sn == 's'){
                    LocalDateTime fechaInicial = LocalDateTime.now();
                    Instant inicio = Instant.now();
                    Backtracking2m b = new Backtracking2m(workshops,fechaInicial,inicio);
                    b.Backtracking(x,0);
                    solucion = b.getSolucion();
                    b.verSolución(solucion);
                }else if(sn == 'n'){
                    LocalDateTime fechaInicial = LocalDateTime.now();
                    Instant inicio = Instant.now();
                    Backtracking2 b = new Backtracking2(workshops,fechaInicial,inicio);
                    b.Backtracking(x,0);
                    solucion = b.getSolucion();
                    b.verSolución(solucion);
                }
                break;
            case 3:
                System.out.println("¿Cuál es el presupuesto disponible? (€)");
                presupuesto = sc.nextFloat();
                if(sn == 's'){
                    LocalDateTime fechaInicial = LocalDateTime.now();
                    Instant inicio = Instant.now();
                    Backtracking3m b = new Backtracking3m(workshops,presupuesto,fechaInicial,inicio);
                    b.Backtracking(x,0);
                    solucion = b.getSolucion();
                    b.verSolución(solucion);

                }else if(sn == 'n'){
                    LocalDateTime fechaInicial = LocalDateTime.now();
                    Instant inicio = Instant.now();
                    Backtracking3 b = new Backtracking3(workshops,presupuesto,fechaInicial,inicio);
                    b.Backtracking(x,0);
                    solucion = b.getSolucion();
                    b.verSolución(solucion);
                }
                break;
            default:
                System.out.println("Opcion incorrecta");
        }

    }


}

