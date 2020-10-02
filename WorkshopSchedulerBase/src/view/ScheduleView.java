package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduleView extends JFrame {

    private final static String[] COL_TITLES = {
            "LUNES",
            "MARTES",
            "MIÉRCOLES",
            "JUEVES",
            "VIERNES"
    };

    private final static String[] ROW_TITLES = {
            "08:00 - 08:50",
            "09:00 - 09:50",
            "10:10 - 11:00",
            "11:10 - 12:00",
            "12:10 - 13:00",
            "13:10 - 14:00",
            "15:30 - 16:20",
            "16:30 - 17:20",
            "17:30 - 18:20",
            "18:30 - 19:20",
            "19:30 - 20:20",
            "20:30 - 21:20"
    };

    private final static String TITLE = "WorkshopSchedule";

    private final static int WIDTH = 1280;
    private final static int HEIGHT = 720;

    private final static float HEADERS_FONT_SIZE = 20f;
    private final static float HEADERS_INFO_FONT_SIZE = 18f;
    private final static float CONTENT_FONT_SIZE = 15f;

    private final static int MAX_TEXT_LENGTH = 25;

    private final static int TITLE_BORDER = 2;
    private final static int CONTENT_BORDER = 1;

    private final static Color TITLE_BACKGROUND_COLOR = Color.GRAY;

    private final static String CATEGORY_INFO = "Categoría: %d";
    private final static String MONEY_INFO = "Coste: %.2f€";

    private final static String BASIC_INFO_TITLE = "General";
    private final static String TIME_INFO_TITLE = "Optimización de horas (opción 2)";
    private final static String COST_INFO_TITLE = "Ajuste al presupuesto (opción 3)";

    private final static String START_DATE_TITLE = "Fecha inicial: %s";
    private final static String FINISH_DATE_TITLE = "Fecha final: %s";
    private final static String DURATION_TITLE = "Tiempo transcurrido: %s";
    private final static String SOLUTIONS_TITLE = "Soluciones encontradas: %s";

    private final static String WORKSHOPS_TITLE = "Talleres totales: %s";
    private final static String TOTAL_HOURS_TITLE = "Horas totales: %s";

    private final static String LIMIT_COST_TITLE = "Precio límite: %s €";
    private final static String BASE_COST_TITLE = "Precio base: %s €";
    private final static String DISCOUNT_TITLE = "Descuento: %s %%";
    private final static String FINAL_COST_TITLE = "Precio final: %s €";
    private final static String CATEGORY_TITLE = "Categoría %d: %s";

    private final static String BLANK_DATE = "--/--/---- - --:--:--";
    private final static String BLANK_DURATION = "--:--:--.---";

    private final static String DATE_FORMAT = "dd/MM/yyyy - HH:mm:ss";
    private final static String DURATION_FORMAT = "%d:%02d:%02d.%03d";
    private final static String ARRAY_FORMAT = "[%s]";
    private final static String COST_FORMAT = "%.2f";

    private final int rows = ROW_TITLES.length;
    private final int cols = COL_TITLES.length;

    private final JPanel mainPanel;
    private final CenteredPanel[][] gridPanels;

    private final JLabel startDateLabel;
    private final JLabel finishDateLabel;
    private final JLabel durationLabel;
    private final JLabel solutionsLabel;

    private final JLabel totalWorkshopsLabel;
    private final JLabel totalTimeLabel;

    private final JLabel baseCostLabel;
    private final JLabel discountLabel;
    private final JLabel finalCostLabel;
    private final JLabel limitCostLabel;

    private final JLabel category1Label;
    private final JLabel category2Label;
    private final JLabel category3Label;
    private final JLabel category4Label;
    private final JLabel category5Label;

    public ScheduleView() {

        //Config basics
        setSize(WIDTH, HEIGHT);
        setResizable(true);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Set base JPanel
        JPanel basePanel = new JPanel(new BorderLayout());
        setContentPane(basePanel);

        //Set main JPanel
        mainPanel = new JPanel(new GridLayout(rows + 1, cols + 1));
        basePanel.add(mainPanel, BorderLayout.CENTER);

        //Create grid
        gridPanels = new CenteredPanel[rows][cols];
        for(int i = 0; i < rows + 1; i++) {
            for(int j = 0; j < cols + 1; j++) {

                //Distinguish content from titles
                CenteredPanel gridPanel;
                if(i == 0) {
                    if(j == 0) {    //First cell
                        gridPanel = new CenteredPanel(false, 0);
                        gridPanel.setPanelBorder(BorderFactory.createMatteBorder(0, 0, TITLE_BORDER, TITLE_BORDER, Color.BLACK));
                    } else {        //Column title
                        gridPanel = new CenteredPanel(false, HEADERS_FONT_SIZE);
                        gridPanel.setPanelBorder(BorderFactory.createMatteBorder(TITLE_BORDER, 0, TITLE_BORDER, TITLE_BORDER, Color.BLACK));
                        gridPanel.setPanelBackground(TITLE_BACKGROUND_COLOR);
                        gridPanel.setPanelText(COL_TITLES[j - 1]);
                    }
                } else {
                    if(j == 0) {    //Row title
                        gridPanel = new CenteredPanel(false, HEADERS_FONT_SIZE);
                        gridPanel.setPanelBorder(BorderFactory.createMatteBorder(0, TITLE_BORDER, TITLE_BORDER, TITLE_BORDER, Color.BLACK));
                        gridPanel.setPanelBackground(TITLE_BACKGROUND_COLOR);
                        gridPanel.setPanelText(ROW_TITLES[i - 1]);
                    } else {        //Content panel
                        gridPanel = new CenteredPanel(true, CONTENT_FONT_SIZE);
                        gridPanel.setPanelBorder(BorderFactory.createMatteBorder(0, 0, CONTENT_BORDER, CONTENT_BORDER, Color.BLACK));
                        gridPanels[i - 1][j - 1] = gridPanel;
                    }
                }

                mainPanel.add(gridPanel);

            }
        }

        //Set information JPanel
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        basePanel.add(infoPanel, BorderLayout.SOUTH);

        //Set generic information JPanel
        JPanel basicInfoPanel = new JPanel();
        basicInfoPanel.setLayout(new BoxLayout(basicInfoPanel, BoxLayout.PAGE_AXIS));
        TitledBorder basicInfoBorder = BorderFactory.createTitledBorder(BASIC_INFO_TITLE);
        basicInfoBorder.setTitleFont(basicInfoBorder.getTitleFont().deriveFont(HEADERS_INFO_FONT_SIZE));
        basicInfoPanel.setBorder(basicInfoBorder);
        infoPanel.add(basicInfoPanel);

        //Set generic information content
        startDateLabel = new JLabel(String.format(START_DATE_TITLE, BLANK_DATE), JLabel.LEFT);
        startDateLabel.setFont(startDateLabel.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        basicInfoPanel.add(startDateLabel);

        finishDateLabel = new JLabel(String.format(FINISH_DATE_TITLE, BLANK_DATE), JLabel.LEFT);
        finishDateLabel.setFont(finishDateLabel.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        basicInfoPanel.add(finishDateLabel);

        durationLabel = new JLabel(String.format(DURATION_TITLE, BLANK_DURATION), JLabel.LEFT);
        durationLabel.setFont(durationLabel.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        basicInfoPanel.add(durationLabel);

        solutionsLabel = new JLabel(String.format(SOLUTIONS_TITLE, "-"), JLabel.LEFT);
        solutionsLabel.setFont(solutionsLabel.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        basicInfoPanel.add(solutionsLabel);

        //Set optimization time information
        JPanel timeInfoPanel = new JPanel();
        timeInfoPanel.setLayout(new BoxLayout(timeInfoPanel, BoxLayout.PAGE_AXIS));
        TitledBorder lostTimeInfoBorder = BorderFactory.createTitledBorder(TIME_INFO_TITLE);
        lostTimeInfoBorder.setTitleFont(lostTimeInfoBorder.getTitleFont().deriveFont(HEADERS_INFO_FONT_SIZE));
        timeInfoPanel.setBorder(lostTimeInfoBorder);
        infoPanel.add(timeInfoPanel);

        //Set lost time information content
        totalWorkshopsLabel = new JLabel(String.format(WORKSHOPS_TITLE, "-"), JLabel.LEFT);
        totalWorkshopsLabel.setFont(totalWorkshopsLabel.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        timeInfoPanel.add(totalWorkshopsLabel);

        totalTimeLabel = new JLabel(String.format(TOTAL_HOURS_TITLE, "-"), JLabel.LEFT);
        totalTimeLabel.setFont(totalTimeLabel.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        timeInfoPanel.add(totalTimeLabel);

        //Set cost information
        JPanel costInfoPanel = new JPanel();
        costInfoPanel.setLayout(new GridLayout(5, 2));
        TitledBorder costInfoBorder = BorderFactory.createTitledBorder(COST_INFO_TITLE);
        costInfoBorder.setTitleFont(costInfoBorder.getTitleFont().deriveFont(HEADERS_INFO_FONT_SIZE));
        costInfoPanel.setBorder(costInfoBorder);
        infoPanel.add(costInfoPanel);

        //Set cost information content
        limitCostLabel = new JLabel(String.format(LIMIT_COST_TITLE, "-"), JLabel.LEFT);
        limitCostLabel.setFont(limitCostLabel.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        costInfoPanel.add(limitCostLabel);

        category1Label = new JLabel(String.format(CATEGORY_TITLE, 1, "-"), JLabel.LEFT);
        category1Label.setFont(category1Label.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        costInfoPanel.add(category1Label);

        baseCostLabel = new JLabel(String.format(BASE_COST_TITLE, "-"), JLabel.LEFT);
        baseCostLabel.setFont(baseCostLabel.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        costInfoPanel.add(baseCostLabel);

        category2Label = new JLabel(String.format(CATEGORY_TITLE, 2, "-"), JLabel.LEFT);
        category2Label.setFont(category2Label.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        costInfoPanel.add(category2Label);

        discountLabel = new JLabel(String.format(DISCOUNT_TITLE, "-"), JLabel.LEFT);
        discountLabel.setFont(discountLabel.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        costInfoPanel.add(discountLabel);

        category3Label = new JLabel(String.format(CATEGORY_TITLE, 3, "-"), JLabel.LEFT);
        category3Label.setFont(category3Label.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        costInfoPanel.add(category3Label);

        finalCostLabel = new JLabel(String.format(FINAL_COST_TITLE, "-"), JLabel.LEFT);
        finalCostLabel.setFont(finalCostLabel.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        costInfoPanel.add(finalCostLabel);

        category4Label = new JLabel(String.format(CATEGORY_TITLE, 4, "-"), JLabel.LEFT);
        category4Label.setFont(category4Label.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        costInfoPanel.add(category4Label);

        costInfoPanel.add(new JPanel());

        category5Label = new JLabel(String.format(CATEGORY_TITLE, 5, "-"), JLabel.LEFT);
        category5Label.setFont(category5Label.getFont().deriveFont(CONTENT_FONT_SIZE).deriveFont(Font.PLAIN));
        costInfoPanel.add(category5Label);

    }

    public void setCellContent(String workshop, int category, float money, Color color, int row, int col) {
        if(row >= 0 && row < rows && col >= 0 && col < cols) {

            if(workshop.length() >= MAX_TEXT_LENGTH) {
                workshop = workshop.substring(0, MAX_TEXT_LENGTH - 3);
                workshop += "...";
            }

            gridPanels[row][col].setPanelText(workshop);
            gridPanels[row][col].setPanelSubText1(String.format(CATEGORY_INFO, category));
            gridPanels[row][col].setPanelSubText2(String.format(MONEY_INFO, money));

            gridPanels[row][col].setPanelBackground(color != null ? color : mainPanel.getBackground());

        }
    }

    public void resetCellContent(int row, int col) {
        if(row >= 0 && row < rows && col >= 0 && col < cols) {
            gridPanels[row][col].setPanelText("");
            gridPanels[row][col].setPanelSubText1("");
            gridPanels[row][col].setPanelSubText2("");
            gridPanels[row][col].setPanelBackground(mainPanel.getBackground());
        }
    }

    public void setStartDateContent(LocalDateTime startDate) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        startDateLabel.setText(String.format(START_DATE_TITLE, dtf.format(startDate)));
    }

    public void setFinishDateContent(LocalDateTime finishDate) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        finishDateLabel.setText(String.format(FINISH_DATE_TITLE, dtf.format(finishDate)));
    }

    public void setDurationContent(Duration duration) {
        durationLabel.setText(String.format(DURATION_TITLE, String.format(DURATION_FORMAT, duration.toHours(),
                duration.toMinutesPart(), duration.toSecondsPart(), duration.toMillisPart())));
    }

    public void setSolutionsContent(int solutions) {
        solutionsLabel.setText(String.format(SOLUTIONS_TITLE, solutions));
    }

    public void resetGeneralContent() {
        startDateLabel.setText(String.format(START_DATE_TITLE, BLANK_DATE));
        finishDateLabel.setText(String.format(FINISH_DATE_TITLE, BLANK_DATE));
        durationLabel.setText(String.format(DURATION_TITLE, BLANK_DURATION));
        solutionsLabel.setText(String.format(SOLUTIONS_TITLE, "-"));
    }

    public void setTotalWorkshopsContent(int workshops) {
        totalWorkshopsLabel.setText(String.format(WORKSHOPS_TITLE, workshops));
    }

    public void setTotalHoursContent(int hours) {
        totalTimeLabel.setText(String.format(TOTAL_HOURS_TITLE, hours));
    }

    public void resetTimeOptimizationContent() {
        totalWorkshopsLabel.setText(String.format(WORKSHOPS_TITLE, "-"));
        totalTimeLabel.setText(String.format(TOTAL_HOURS_TITLE, "-"));
    }

    public void setLimitCostContent(float limitCost) {
        limitCostLabel.setText(String.format(LIMIT_COST_TITLE, String.format(COST_FORMAT, limitCost)));
    }

    public void setBaseCostContent(float baseCost) {
        baseCostLabel.setText(String.format(BASE_COST_TITLE, String.format(COST_FORMAT, baseCost)));
    }

    public void setDiscountContent(int discount) {
        discountLabel.setText(String.format(DISCOUNT_TITLE, discount));
    }

    public void setFinalCostContent(float finalCost) {
        finalCostLabel.setText(String.format(FINAL_COST_TITLE, String.format(COST_FORMAT, finalCost)));
    }

    public void setCategoryContent(int category, int total) {
        switch(category) {
            case 1:
                category1Label.setText(String.format(CATEGORY_TITLE, category, total));
                break;
            case 2:
                category2Label.setText(String.format(CATEGORY_TITLE, category, total));
                break;
            case 3:
                category3Label.setText(String.format(CATEGORY_TITLE, category, total));
                break;
            case 4:
                category4Label.setText(String.format(CATEGORY_TITLE, category, total));
                break;
            case 5:
                category5Label.setText(String.format(CATEGORY_TITLE, category, total));
                break;
        }
    }

    public void resetCostOptimizationContent() {

        limitCostLabel.setText(String.format(LIMIT_COST_TITLE, "-"));
        baseCostLabel.setText(String.format(BASE_COST_TITLE, "-"));
        discountLabel.setText(String.format(DISCOUNT_TITLE, "-"));
        finalCostLabel.setText(String.format(FINAL_COST_TITLE, "-"));

        category1Label.setText(String.format(CATEGORY_TITLE, 1, "-"));
        category2Label.setText(String.format(CATEGORY_TITLE, 2, "-"));
        category3Label.setText(String.format(CATEGORY_TITLE, 3, "-"));
        category4Label.setText(String.format(CATEGORY_TITLE, 4, "-"));
        category5Label.setText(String.format(CATEGORY_TITLE, 5, "-"));

    }

    public void resetAllContents() {

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                resetCellContent(i, j);
            }
        }

        resetGeneralContent();
        resetTimeOptimizationContent();
        resetCostOptimizationContent();

    }

}
