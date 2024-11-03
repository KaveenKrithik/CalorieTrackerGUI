import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CalorieTrackerGUI {

    private static final String DB_URL = "jdbc:mysql://localhost/fitness_tracker";
    private static final String USER = "me2anirudh";
    private static final String PASSWORD = "Ani@1112";


    private JFrame frame;
    private JTextField ageField;
    private JTextField weightField;
    private JTextField heightField;
    private JComboBox<String> activityComboBox;
    private JComboBox<String> genderComboBox;
    private JTextArea resultArea;

    private List<FoodItem> foodItems;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                CalorieTrackerGUI window = new CalorieTrackerGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CalorieTrackerGUI() {
        initialize();
        initializeFoodItems();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(8, 2, 10, 10));

        JLabel lblAge = new JLabel("Enter age:");
        panel.add(lblAge);

        ageField = new JTextField();
        panel.add(ageField);

        JLabel lblWeight = new JLabel("Enter weight (kg):");
        panel.add(lblWeight);

        weightField = new JTextField();
        panel.add(weightField);

        JLabel lblHeight = new JLabel("Enter height (cm):");
        panel.add(lblHeight);

        heightField = new JTextField();
        panel.add(heightField);

        JLabel lblActivityLevel = new JLabel("Select activity level:");
        panel.add(lblActivityLevel);

        String[] activityLevels = {"Sedentary", "Lightly Active", "Moderately Active", "Very Active"};
        activityComboBox = new JComboBox<>(activityLevels);
        panel.add(activityComboBox);

        JLabel lblGender = new JLabel("Select gender:");
        panel.add(lblGender);

        String[] genders = {"Male", "Female"};
        genderComboBox = new JComboBox<>(genders);
        panel.add(genderComboBox);

        JButton btnCalculate = new JButton("Calculate Calories");
        btnCalculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateCalories();
            }
        });
        panel.add(btnCalculate);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        panel.add(resultArea);

        JButton btnTrackFoodIntake = new JButton("Track Food Intake");
        btnTrackFoodIntake.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trackFoodIntake();
            }
        });
        panel.add(btnTrackFoodIntake);
    }

    private void initializeFoodItems() {
        foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("Apple", 95));
        foodItems.add(new FoodItem("Banana", 105));
        foodItems.add(new FoodItem("Chicken Breast", 165));
        foodItems.add(new FoodItem("Salad", 50));
        foodItems.add(new FoodItem("Bread", 74));
        foodItems.add(new FoodItem("Dal", 128));
        foodItems.add(new FoodItem("Roti", 85));
        foodItems.add(new FoodItem("Yogurt", 75));
        foodItems.add(new FoodItem("Pulao", 133));
        foodItems.add(new FoodItem("Rajma", 121));
        foodItems.add(new FoodItem("Protein Shake", 54));
        foodItems.add(new FoodItem("Rice", 150));
        foodItems.add(new FoodItem("Peanut Butter", 100));
        foodItems.add(new FoodItem("Coffee", 72));


    }

    private void calculateCalories() {
        int age = Integer.parseInt(ageField.getText());
        double weight = Double.parseDouble(weightField.getText());
        double height = Double.parseDouble(heightField.getText());
        String activityLevel = (String) activityComboBox.getSelectedItem();
        String gender = (String) genderComboBox.getSelectedItem();

        double activityMultiplier = getActivityMultiplier(activityLevel);
        double genderMultiplier = getGenderMultiplier(gender);

        double maintenanceCalories = calculateMaintenanceCalories(age, weight, height, activityMultiplier, genderMultiplier);
        resultArea.setText("Maintenance Calories: " + maintenanceCalories + " kcal");
    }

    private double getActivityMultiplier(String activityLevel) {
        switch (activityLevel) {
            case "Sedentary":
                return 1.2;
            case "Lightly Active":
                return 1.375;
            case "Moderately Active":
                return 1.55;
            case "Very Active":
                return 1.725;
            default:
                return 1.0;
        }
    }

    private double getGenderMultiplier(String gender) {
        return (gender.equals("Male")) ? 1.0 : 0.9;
    }

    private double calculateMaintenanceCalories(int age, double weight, double height, double activityMultiplier, double genderMultiplier) {
        double bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        return bmr * activityMultiplier * genderMultiplier;
    }

    private void trackFoodIntake() {
        JFrame foodIntakeFrame = new JFrame("Food Intake Tracker");
        foodIntakeFrame.setBounds(100, 100, 400, 300);
        foodIntakeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        foodIntakeFrame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblFoodItem = new JLabel("Select Food Item:");
        panel.add(lblFoodItem);

        String[] foodItemNames = foodItems.stream().map(FoodItem::getName).toArray(String[]::new);
        JComboBox<String> foodItemComboBox = new JComboBox<>(foodItemNames);  // Declaration here
        panel.add(foodItemComboBox);

        JLabel lblCalories = new JLabel("Calories:");
        panel.add(lblCalories);

        JTextField caloriesField = new JTextField();
        caloriesField.setEditable(false);
        panel.add(caloriesField);

        JButton btnAddFoodItem = new JButton("Add Food Item");
        btnAddFoodItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Use foodItemComboBox here
                addFoodItemToTotal(caloriesField, foodItemComboBox);
            }
        });
        panel.add(btnAddFoodItem);

        JButton btnTotalCalories = new JButton("Total Calories");
        btnTotalCalories.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateTotalCalories(caloriesField);
            }
        });
        panel.add(btnTotalCalories);

        foodIntakeFrame.setVisible(true);
    }

    private void addFoodItemToTotal(JTextField caloriesField, JComboBox<String> foodItemComboBox) {
        String selectedFoodItemName = (String) foodItems.get(foodItemComboBox.getSelectedIndex()).getName();
        int selectedFoodItemCalories = foodItems.get(foodItemComboBox.getSelectedIndex()).getCalories();

        resultArea.append("\nFood Item: " + selectedFoodItemName + ", Calories: " + selectedFoodItemCalories);
    }


    private void calculateTotalCalories(JTextField caloriesField) {
        int totalCalories = 0;
        String[] lines = resultArea.getText().split("\n");
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                totalCalories += Integer.parseInt(parts[1].trim().split(" ")[1]);
            }
        }
        caloriesField.setText(String.valueOf(totalCalories));
    }

    private static class FoodItem {
        private String name;
        private int calories;

        public FoodItem(String name, int calories) {
            this.name = name;
            this.calories = calories;
        }

        public String getName() {
            return name;
        }

        public int getCalories() {
            return calories;
        }
    }
}