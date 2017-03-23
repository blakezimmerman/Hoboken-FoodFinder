package com.bzimmerman;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.ArrayList;
import java.util.Random;

public class Main
{
    //Initialization of variables
    public static final int NUM_RESTAURANTS = 220;

    public static boolean isDuckbills;
    public static boolean isWashington;

    public static void main(String[] args)
    {
        //Declaration of variables
        isDuckbills = false;
        isWashington = false;

        //Draw the UI
        DrawUI();

    }

    public static void DrawUI()
    {
        //Initialize the window frame
        JFrame mainFrame = new JFrame ("Hoboken Food Finder");
        mainFrame.setSize(500, 400); //(width, height)
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        //Initialize bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 1)); //(rows, columns);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 10, 10)); //top, left, bottom, right
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        //Initialize the result text pane
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        StyledDocument resultText = textPane.getStyledDocument();

        //Add scroll to the pane
        JScrollPane scroll = new JScrollPane(textPane){
            @Override
            public Dimension getPreferredSize()
            {
                int size = (int)(mainFrame.getSize().height * 0.65);
                return new Dimension(0,size);
            }
        };
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        //Add resultText to bottom panel
        bottomPanel.add(scroll, BorderLayout.CENTER);

        //Initialize top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2)); //(rows, columns);
        topPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 10, 10)); //top, left, bottom, right
        mainFrame.add(topPanel, BorderLayout.NORTH);

        //Initialize checkbox panel
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new GridLayout(3, 1)); //(rows, columns);

        JLabel requirements= new JLabel("Requirements:");
        checkboxPanel.add(requirements);

        //Initialize check boxes
        JCheckBox duckbillsCB = new JCheckBox("Accepts DuckBills");
        checkboxPanel.add(duckbillsCB);
        duckbillsCB.setSelected(false);

        JCheckBox washingtonCB = new JCheckBox("On Washington Street");
        checkboxPanel.add(washingtonCB);
        washingtonCB.setSelected(false);

        //Add listener to duckbillsCB
        duckbillsCB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Object source = e.getSource();
                if (source instanceof JCheckBox)
                {
                    JCheckBox cb = (JCheckBox)source;
                    if (cb.isSelected())
                        isDuckbills = true;
                    else
                        isDuckbills = false;
                }
            }
        });

        //Add listener to washingtonCB
        washingtonCB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Object source = e.getSource();
                if (source instanceof JCheckBox)
                {
                    JCheckBox cb = (JCheckBox)source;
                    if (cb.isSelected())
                    { isWashington = true; }
                    else
                    { isWashington = false; }
                }
            }
        });

        //Add checkboxPanel to topPanel
        topPanel.add(checkboxPanel, BorderLayout.WEST);

        //Initialize button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1)); //(rows, columns);

        JLabel padding= new JLabel("");
        buttonPanel.add(padding);

        //Initialize buttons
        JButton randomButton = new JButton ("Suggest Me a Restaurant!");
        buttonPanel.add(randomButton);

        //Add listener to randomButton
        randomButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Object source = e.getSource();
                if (source instanceof JButton)
                {
                    String randResult = getRestaurant(sortRestaurants(createArray()));

                    textPane.setText(randResult);
                    textPane.setCaretPosition(0);

                    int randStartIndex = randResult.indexOf("\n", 0);
                    int randStopIndex = randResult.indexOf("\n", (randStartIndex + 2));

                    if (randStartIndex != -1)
                    {
                        //Stylize Text
                        SimpleAttributeSet randText = new SimpleAttributeSet();
                        StyleConstants.setAlignment(randText, StyleConstants.ALIGN_CENTER);
                        StyleConstants.setFontFamily(randText, "");
                        StyleConstants.setFontSize(randText, 13);
                        resultText.setParagraphAttributes(0, resultText.getLength(), randText, true);

                        //Sub-string specific styles
                        SimpleAttributeSet randTextBold = new SimpleAttributeSet();
                        StyleConstants.setBold(randTextBold, true);
                        StyleConstants.setFontSize(randTextBold, 16);
                        resultText.setCharacterAttributes(randStartIndex+2, (randStopIndex - randStartIndex-1), randTextBold, true);
                    }
                    else
                    {
                        textPane.setText("Error");
                    }
                }
            }
        });

        JButton viewAllButton = new JButton ("View All Restaurants");
        buttonPanel.add(viewAllButton);

        //Add listener to viewAllButton
        viewAllButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Object source = e.getSource();
                if (source instanceof JButton)
                {
                    String listResult = getList(sortRestaurants(createArray()));

                    textPane.setText(listResult);
                    textPane.setCaretPosition(0);

                    //Center all text
                    SimpleAttributeSet allText = new SimpleAttributeSet();
                    StyleConstants.setAlignment(allText, StyleConstants.ALIGN_CENTER);
                    StyleConstants.setFontFamily(allText, "");
                    StyleConstants.setFontSize(allText, 13);
                    StyleConstants.setBold(allText, false);
                    resultText.setParagraphAttributes(0, resultText.getLength(), allText, true);
                    resultText.setCharacterAttributes(0, resultText.getLength(), allText, true);
                }
            }
        });

        //Add buttonPanel to topPanel
        topPanel.add (buttonPanel, BorderLayout.EAST);

        //Set frame to visible
        mainFrame.setVisible(true);
    }

    public static ArrayList<Restaurant> sortRestaurants(Restaurant[] r)
    {
        ArrayList<Restaurant> applicable = new ArrayList<Restaurant>();
        ArrayList<Restaurant> takesDuckbills = new ArrayList<Restaurant>();
        ArrayList<Restaurant> onWashington = new ArrayList<Restaurant>();

        if (isDuckbills)
        {
            for (Restaurant current: r)
            {
                if (current.getDuckbills())
                    takesDuckbills.add(current);
            }
        }

        if (isWashington)
        {
            for (Restaurant current: r)
            {
                if (current.getAddress().contains("Washington Street"))
                    onWashington.add(current);
            }
        }

        if (!takesDuckbills.isEmpty() || !onWashington.isEmpty())
        {
            if (!takesDuckbills.isEmpty())
            {
                if (!onWashington.isEmpty())
                {
                    for (Restaurant current : takesDuckbills)
                    {
                        if (onWashington.contains(current))
                        {
                            if (!applicable.contains(current))
                                applicable.add(current);
                        }
                    }
                }
                else
                {
                    for (Restaurant current : takesDuckbills)
                    {
                            if (!applicable.contains(current))
                                applicable.add(current);
                    }
                }
            }
            if (!onWashington.isEmpty())
            {
                if (!takesDuckbills.isEmpty())
                {
                    for (Restaurant current : onWashington)
                    {
                        if (takesDuckbills.contains(current))
                        {
                            if (!applicable.contains(current))
                                applicable.add(current);
                        }
                    }
                }
                else
                {
                    for (Restaurant current : onWashington)
                    {
                        if (!applicable.contains(current))
                            applicable.add(current);
                    }
                }
            }
        }
        else
        {
            for (Restaurant current : r)
            {
                applicable.add(current);
            }
        }

        return applicable;
    }

    public static String getRestaurant(ArrayList applicable)
    {
        String result;

        Random rand = new Random();
        Restaurant randRest = (Restaurant)applicable.get(rand.nextInt(applicable.size()));

        result = ("Consider eating at...\n\n" + randRest.getName() + "\n" + randRest.getAddress());

        return result;
    }

    public static String getList(ArrayList<Restaurant> applicable)
    {
        String allRest = "";

        for (int i = 0; i < applicable.size(); i++)
        {
            if (i == (applicable.size() - 1))
                allRest += (applicable.get(i).getName() + " — " + applicable.get(i).getAddress());
            else
                allRest += (applicable.get(i).getName() + " — " + applicable.get(i).getAddress() + "\n");
        }

        return allRest;
    }

    public static Restaurant[] createArray()
    {
        Restaurant[] restaurants = new Restaurant[NUM_RESTAURANTS];

        restaurants [0] = new Restaurant ("Lisa's Italian Deli", "901 Park Avenue", true);
        restaurants [1] = new Restaurant ("Asia Chinese Cuisine", "926 Washington Street", true);
        restaurants [2] = new Restaurant ("Bagels on the Hudson", "802 Washington Street", true);
        restaurants [3] = new Restaurant ("Cluck-U Chicken", "112 Washington Street", true);
        restaurants [4] = new Restaurant ("Giovanni's Pizza", "603 Washington Street", true);
        restaurants [5] = new Restaurant ("It's Greek to Me", "538 Washington Street", true);
        restaurants [6] = new Restaurant ("Johnny Rocket’s", "134 Washington Street", true);
        restaurants [7] = new Restaurant ("Qdoba Mexican Grill", "400 Washington Street", true);
        restaurants [8] = new Restaurant ("Stacks Pancake House & Cafe", "506 Washington Street", true);
        restaurants [9] = new Restaurant ("Subway", "418 Washington Street", true);
        restaurants [10] = new Restaurant ("Five Guys Burger & Fries", "319 Washington Street", true);
        restaurants [11] = new Restaurant ("Karma Kafe", "505 Washington Street", true);
        restaurants [12] = new Restaurant ("Mr. Wrap’s", "741 Garden Street", true);
        restaurants [13] = new Restaurant ("The Taco Truck", "62 Newark Street", true);
        restaurants [14] = new Restaurant ("Vito’s Italian Deli", "806 Washington Street", true);
        restaurants [15] = new Restaurant ("16 Handles", "1185 Maxwell Lane ", true);
        restaurants [16] = new Restaurant ("7 Eleven", "422 Washington Street", true);
        restaurants [17] = new Restaurant ("Bareburger", "515 Washington Street", true);
        restaurants [18] = new Restaurant ("Benny Tudino's Pizzeria Restaurant", "622 Washington Street", true);
        restaurants [19] = new Restaurant ("Boardwalk Fresh Burgers and Fries", "832 Washington Street", true);
        restaurants [20] = new Restaurant ("Crepe Guru", "315 Washington Street", true);
        restaurants [21] = new Restaurant ("Dunkin Donuts", "700 Washington Street", true);
        restaurants [22] = new Restaurant ("Midtown Philly Steaks", "523 Washington Street", true);
        restaurants [23] = new Restaurant ("Muscle Maker Grill", "217 Washington Street", true);
        restaurants [24] = new Restaurant ("Napoli's", "1118 Washington Street", true);
        restaurants [25] = new Restaurant ("Planet Mac", "219 Washington Street", true);
        restaurants [26] = new Restaurant ("Slider Street", "138 Park Avenue", true);
        restaurants [27] = new Restaurant ("Taco Truck", "62 Newark Street", true);
        restaurants [28] = new Restaurant ("1 Republic", "221 Washington Street", false);
        restaurants [29] = new Restaurant ("10th and Willow", "935 Willow Avenue", false);
        restaurants [30] = new Restaurant ("Atomic Wings", "618 Washington Street", false);
        restaurants [31] = new Restaurant ("Ali Baba", "912 Washington Street", false);
        restaurants [32] = new Restaurant ("Ayame Hibachi & Sushi", "526 Washington Street", false);
        restaurants [33] = new Restaurant ("Alucra Pizzeria", "301 Jackson Street", false);
        restaurants [34] = new Restaurant ("Amanda's Restaurant", "908 Washington Street", false);
        restaurants [35] = new Restaurant ("Anthony David's", "953 Bloomfield Street", false);
        restaurants [36] = new Restaurant ("Antique Bakery", "122 Willow Avenue", false);
        restaurants [37] = new Restaurant ("Arthur's Tavern", "237 Washington Street", false);
        restaurants [38] = new Restaurant ("Augustino's Restaurant", "1104 Washington Street", false);
        restaurants [39] = new Restaurant ("Boardwalk Fresh Burgers & Fries", "832 Washington Street", false);
        restaurants [40] = new Restaurant ("ZYLO Tuscan Steak House", "225 River Street", false);
        restaurants [41] = new Restaurant ("Bagel Smashery", "153 First Street", false);
        restaurants [42] = new Restaurant ("Basile's Pizza", "89 Washington Street", false);
        restaurants [43] = new Restaurant ("Big Banner Restaurant", "401 Jackson Street", false);
        restaurants [44] = new Restaurant ("Biggie's Clam Bar", "318 Madison Street", false);
        restaurants [45] = new Restaurant ("Biggie's on Newark Street", "42 Newark Street", false);
        restaurants [46] = new Restaurant ("Baja Mexican Restaurant", "104 14th Street", false);
        restaurants [47] = new Restaurant ("Bin 14 Trattoria & Wine Bar", "1314 Washington Street", false);
        restaurants [48] = new Restaurant ("Black Bear Bar & Grill", "205 Washington Street", false);
        restaurants [49] = new Restaurant ("Black Rail Coffee", "800 Jackson Street", false);
        restaurants [50] = new Restaurant ("Blimpie Subs & Salads", "110 Washington Street", false);
        restaurants [51] = new Restaurant ("Brasserie de Paris", "700 First Street", false);
        restaurants [52] = new Restaurant ("Bangkok City", "335 Washington Street", false);
        restaurants [53] = new Restaurant ("Ben & Jerry's", "405 Washington Street", false);
        restaurants [54] = new Restaurant ("bwè kafe", "1002 Washington Street", false);
        restaurants [55] = new Restaurant ("Court Street", "61 6th Street, Hoboken", false);
        restaurants [56] = new Restaurant ("Cafe Matt & Meera", "618 Washington Street", false);
        restaurants [57] = new Restaurant ("Cafe Michelina", "423 Bloomfield Street", false);
        restaurants [58] = new Restaurant ("Casual Thai", "1006 Washington Street", false);
        restaurants [59] = new Restaurant ("Chicken Factory", "529 Washington Street", false);
        restaurants [60] = new Restaurant ("Clem's Steaks", "79 Hudson Street", false);
        restaurants [61] = new Restaurant ("Cold Stone Creamery", "116 Washington Street", false);
        restaurants [62] = new Restaurant ("Crepes & Things", "123 Washington Street", false);
        restaurants [63] = new Restaurant ("Cugini Kitchen", "918 Washington Street", false);
        restaurants [64] = new Restaurant ("Cadillac Cantina", "80 River Street", false);
        restaurants [65] = new Restaurant ("Carlo's City Hall Bake Shop", "95 Washington Street", false);
        restaurants [66] = new Restaurant ("Carpe Diem", "1405 Grand Street", false);
        restaurants [67] = new Restaurant ("Charrito's", "121 or 1024 Washington Street", false);
        restaurants [68] = new Restaurant ("The Cheese Store", "720 Monroe Street", false);
        restaurants [69] = new Restaurant ("Chen's Kitchen", "301 Jackson Street", false);
        restaurants [70] = new Restaurant ("Chipotle Mexican Grill", "229 Washington Street", false);
        restaurants [71] = new Restaurant ("Choc O Pain Bakery", "157 First Street", false);
        restaurants [72] = new Restaurant ("City Bistro", "56 14th Street", false);
        restaurants [73] = new Restaurant ("Cucharamama", "233 Clinton Street", false);
        restaurants [74] = new Restaurant ("D's Soul Full Cafe", "918 Willow Avenue", false);
        restaurants [75] = new Restaurant ("Fran's Italian Deli", " 202 Hudson Street", false);
        restaurants [76] = new Restaurant ("Margherita's Cafe", "740 Washington Street", false);
        restaurants [77] = new Restaurant ("Rice Shop", "304 Washington Street", false);
        restaurants [78] = new Restaurant ("Hudson Pizza Company", "100 Hudson Street", false);
        restaurants [79] = new Restaurant ("Rita’s Ice", "121 Washington Street", false);
        restaurants [80] = new Restaurant ("Delfino's Pizzeria", "500 Jefferson Street", false);
        restaurants [81] = new Restaurant ("Dino & Harry's", "163 14th Street", false);
        restaurants [82] = new Restaurant ("Delight Deli & Grocery", "56 Monroe Street", false);
        restaurants [83] = new Restaurant ("Domino's Pizza", "462 Newark Street", false);
        restaurants [84] = new Restaurant ("Dom's Bakery Grand", "506 Grand Street", false);
        restaurants [85] = new Restaurant ("Dozzino", "534 Adams Street", false);
        restaurants [86] = new Restaurant ("Dubliner", "96 River Street", false);
        restaurants [87] = new Restaurant ("East LA", "508 Washington Street", false);
        restaurants [88] = new Restaurant ("Elysian Cafe", "1001 Washington Street", false);
        restaurants [89] = new Restaurant ("Empire Coffee & Tea", "338 Bloomfield Street", false);
        restaurants [90] = new Restaurant ("Fresh Tortillas Grill", "514 Washington Street", false);
        restaurants [91] = new Restaurant ("Fresh U Grill & Juice Bar", "70 Hudson Street", false);
        restaurants [92] = new Restaurant ("Farside Tavern", "531 Washington Street", false);
        restaurants [93] = new Restaurant ("Finnegan's Pub", "734 Willow Avenue", false);
        restaurants [94] = new Restaurant ("Fiore's House Of Quality", "414 Adams Street", false);
        restaurants [95] = new Restaurant ("Flamboyan Restaurant", "1000 Willow Avenue", false);
        restaurants [96] = new Restaurant ("Grande Pizza", "400 Newark Street", false);
        restaurants [97] = new Restaurant ("Ganache at the Hudson Tea", "1500 Hudson Street", false);
        restaurants [98] = new Restaurant ("Gateway Bake Shop", "1 Hudson Place", false);
        restaurants [99] = new Restaurant ("General Store Deli & Pizzeria", "509 Willow Avenue", false);
        restaurants [100] = new Restaurant ("Giorgio Pasticcerie Italian", "1112 Washington Street", false);
        restaurants [101] = new Restaurant ("Green Garden", "1202 Washington Street", false);
        restaurants [102] = new Restaurant ("Green Rock Tap & Grill", "70 Hudson Street", false);
        restaurants [103] = new Restaurant ("Grimaldi's on Washington", "411 Washington Street", false);
        restaurants [104] = new Restaurant ("Health Grill", "150-154 14th Street", false);
        restaurants [105] = new Restaurant ("Hoboken Burrito", "209 4th Street", false);
        restaurants [106] = new Restaurant ("Hoboken Gourmet Company", "423 Washington Street", false);
        restaurants [107] = new Restaurant ("Havana Cafe & Lounge", "32 Newark Street", false);
        restaurants [108] = new Restaurant ("Hoboken Bar & Grill", "230 Washington Street", false);
        restaurants [109] = new Restaurant ("Hoboken Hot Bagels", "634 Washington Street", false);
        restaurants [110] = new Restaurant ("Hot House Pizza", "606 2nd Street", false);
        restaurants [111] = new Restaurant ("Hotel Victor Bar & Grill", "77 Hudson Place", false);
        restaurants [112] = new Restaurant ("Hudson Tavern", "51-53 14th Street", false);
        restaurants [113] = new Restaurant ("Illuzion", "337 Washington Street", false);
        restaurants [114] = new Restaurant ("Imposto's Pizzeria", "102 Washington Street", false);
        restaurants [115] = new Restaurant ("Istana Chinese Cuisine", "936 Washington Street", false);
        restaurants [116] = new Restaurant ("India on the Hudson", "1210 Washington Street", false);
        restaurants [117] = new Restaurant ("Jack's Cabin", "9 Patterson Avenue", false);
        restaurants [118] = new Restaurant ("Jimmy John's", "96 Hudson Street", false);
        restaurants [119] = new Restaurant ("Johnny Pepperoni", "219 11th Street", false);
        restaurants [120] = new Restaurant ("JP & Sunrise Bagels", "52 Newark Street", false);
        restaurants [121] = new Restaurant ("Las Olas Sushi Bar & Grill", "1319 Washington Street", false);
        restaurants [122] = new Restaurant ("Leo's Grandevous", "200 Grand Street", false);
        restaurants [123] = new Restaurant ("Little Market", "400 Newark Street", false);
        restaurants [124] = new Restaurant ("Little Grocery", "1212 Washington Street", false);
        restaurants [125] = new Restaurant ("Love & Yogurt", "54 Newark Street", false);
        restaurants [126] = new Restaurant ("Luca Brasi's Italian Deli", "100 Park Avenue", false);
        restaurants [127] = new Restaurant ("La Bouche Cafe", "103 Garden Street", false);
        restaurants [128] = new Restaurant ("La Isla Restaurant", "104 Washington Street", false);
        restaurants [129] = new Restaurant ("Lepore's Chocolates", "537 Garden Street", false);
        restaurants [130] = new Restaurant ("Liberty Gourmet", "1100 Adams Street", false);
        restaurants [131] = new Restaurant ("Lo-Fatt Chow", "720 Monroe Street, Suite 103", false);
        restaurants [132] = new Restaurant ("Luke's Lobsters", "207 Washington Street", false);
        restaurants [133] = new Restaurant ("Mamoun's Falafel Restaurant", "502 Washington Street", false);
        restaurants [134] = new Restaurant ("Mikie Squared", "616 Washington Street", false);
        restaurants [135] = new Restaurant ("M & P Biancamano", "1116 Washington Street", false);
        restaurants [136] = new Restaurant ("Madison Bar & Grill", "1316 Washington Street", false);
        restaurants [137] = new Restaurant ("Malibu Diner", "257 14th Street", false);
        restaurants [138] = new Restaurant ("Mario’s Classic Pizza", "742 Garden Street", false);
        restaurants [139] = new Restaurant ("Maroon", "638 Willow Avenue", false);
        restaurants [140] = new Restaurant ("Marty O'Brien's", "94 Bloomfield Street", false);
        restaurants [141] = new Restaurant ("Maxwell's Tavern", "1039 Washington Street", false);
        restaurants [142] = new Restaurant ("McDonald’s", "234 Washington Street", false);
        restaurants [143] = new Restaurant ("McLoone's Pier House","1300 Sinatra Drive North", false);
        restaurants [144] = new Restaurant ("McSwiggan's Pub", "110 First Street", false);
        restaurants [145] = new Restaurant ("Mill's Tavern", "125 Washington Street", false);
        restaurants [146] = new Restaurant ("Mulligan's Pub", "159 First Street", false);
        restaurants [147] = new Restaurant ("No. 1 Chinese Cuisine", "642 Washington Street", false);
        restaurants [148] = new Restaurant ("Otto Strada", "743 Park Avenue", false);
        restaurants [149] = new Restaurant ("Old Lorenzos", "301 Jackson Street", false);
        restaurants [150] = new Restaurant ("Off the Wall", "512 Washington Street", false);
        restaurants [151] = new Restaurant ("Okinawa Sushi & Grill", "400 Newark Street", false);
        restaurants [152] = new Restaurant ("Old German Bakery", "332 Washington Street", false);
        restaurants [153] = new Restaurant ("Onieal's", "343 Park Avenue", false);
        restaurants [154] = new Restaurant ("Pho' Nomenon", "516 Washington Street", false);
        restaurants [155] = new Restaurant ("Pizza Republic", "406 Washington Street", false);
        restaurants [156] = new Restaurant ("Pure Pita", "324 Washington Street", false);
        restaurants [157] = new Restaurant ("Panello Italian Restaurant", "720 Monroe Street, Suite 105", false);
        restaurants [158] = new Restaurant ("Panera Bread", "308 Washington Street", false);
        restaurants [159] = new Restaurant ("Papaya Dog", "55 Newark Street", false);
        restaurants [160] = new Restaurant ("Piccolo's Clam Bar", "92 Clinton Street", false);
        restaurants [161] = new Restaurant ("Pilsener Haus & Biergarten", "1422 Grand Street", false);
        restaurants [162] = new Restaurant ("Puerto Spain", "116 14th Street", false);
        restaurants [163] = new Restaurant ("Precious", "128 Washington Street", false);
        restaurants [164] = new Restaurant ("Raf Deli", "552 First Street", false);
        restaurants [165] = new Restaurant ("re.Juice.a.Nation", "64 Newark Street", false);
        restaurants [166] = new Restaurant ("Red Mango", "213 Washington Street", false);
        restaurants [167] = new Restaurant ("Robongi", "520 Washington Street", false);
        restaurants [168] = new Restaurant ("Rome Pizza", "20 Hudson Place", false);
        restaurants [169] = new Restaurant ("Rosticeria Da Gigi", "916 Washington Street", false);
        restaurants [170] = new Restaurant ("Red Lion Coffee Co.", "1320 Bloomfield Street", false);
        restaurants [171] = new Restaurant ("Riverside Deli", "205 Hudson Street", false);
        restaurants [172] = new Restaurant ("Rosario's At Willow", "1132 Willow Avenue", false);
        restaurants [173] = new Restaurant ("San Giuseppe", "1320 Adams Street", false);
        restaurants [174] = new Restaurant ("Sasso's", "1038 Garden Street", false);
        restaurants [175] = new Restaurant ("Seven Star Pizza", "342 Garden Street", false);
        restaurants [176] = new Restaurant ("Smokin' Barrel", "1313 Willow Avenue", false);
        restaurants [177] = new Restaurant ("Sri Thai", "234 Bloomfield Street", false);
        restaurants [178] = new Restaurant ("Sushi House of Hoboken", "155 First Street", false);
        restaurants [179] = new Restaurant ("Sushi Lounge", "200 Hudson Street", false);
        restaurants [180] = new Restaurant ("Sabores", "518 Washington Street", false);
        restaurants [181] = new Restaurant ("Saint Mary Pizzeria", "131 Willow Avenue", false);
        restaurants [182] = new Restaurant ("Sapori Catering", "1320-1322 Adams Street", false);
        restaurants [183] = new Restaurant ("Satay Chinese Cuisine", "99 Washington Street", false);
        restaurants [184] = new Restaurant ("Satay Malaysian Cuisine", "99 Washington Street", false);
        restaurants [185] = new Restaurant ("Sbarro Pizza", "1 Hudson Place", false);
        restaurants [186] = new Restaurant ("Schnackenberg's Luncheonette", "1110 Washington Street", false);
        restaurants [187] = new Restaurant ("Sobsey's Produce", "92 Bloomfield Street", false);
        restaurants [188] = new Restaurant ("Spa Restaurant", "74 Hudson Street", false);
        restaurants [189] = new Restaurant ("Sweet", "343 Garden Street", false);
        restaurants [190] = new Restaurant ("The Brass Rail", "135 Washington Street", false);
        restaurants [191] = new Restaurant ("The Stewed Cow", "400 Adams Street", false);
        restaurants [192] = new Restaurant ("Tony Boloney's", "267 First Street", false);
        restaurants [193] = new Restaurant ("T Thai Grill & Seafood", "102 Hudson Street", false);
        restaurants [194] = new Restaurant ("The Brick", "1122 Washington Street", false);
        restaurants [195] = new Restaurant ("The Clinton Social", "700 Clinton Street", false);
        restaurants [196] = new Restaurant ("Tilted Kilt Pub & Eatery", "800 Jackson Street", false);
        restaurants [197] = new Restaurant ("Turning Point", "1420 Frank Sinatra Drive", false);
        restaurants [198] = new Restaurant ("Teak on the Hudson", "16 Hudson Place", false);
        restaurants [199] = new Restaurant ("Texas-Arizona", "76 River Street", false);
        restaurants [200] = new Restaurant ("Thai Elephant", "1 14th Street", false);
        restaurants [201] = new Restaurant ("The Cuban", "333 Washington Street", false);
        restaurants [202] = new Restaurant ("The Fig Tree", "306-308 Park Avenue", false);
        restaurants [203] = new Restaurant ("The Turtle Club", "936 Park Avenue", false);
        restaurants [204] = new Restaurant ("Three-A's Olde Bar & Grill", "500 Grand Street", false);
        restaurants [205] = new Restaurant ("Tony's Italian Bakery & Deli", "410 2nd Street", false);
        restaurants [206] = new Restaurant ("Torna's Pizzeria", "252 9th Street", false);
        restaurants [207] = new Restaurant ("Trattoria Saporito", "328 Washington Street", false);
        restaurants [208] = new Restaurant ("Trinity", "306 Sinatra Drive", false);
        restaurants [209] = new Restaurant ("Uptown Pizzeria", "54 14th Street", false);
        restaurants [210] = new Restaurant ("Ubu Japanese Restaurant", "205 Hudson Street", false);
        restaurants [211] = new Restaurant ("Ultramarinos", "260 3rd Street", false);
        restaurants [212] = new Restaurant ("Wings to Go", "400 Newark Street", false);
        restaurants [213] = new Restaurant ("Wicked Wolf Tavern", "120 Sinatra Drive", false);
        restaurants [214] = new Restaurant ("Willie McBrides", "616 Grand Street", false);
        restaurants [215] = new Restaurant ("Willow Deli Plus", "228 Willow Avenue", false);
        restaurants [216] = new Restaurant ("Yeung II", "1120 Washington Street", false);
        restaurants [217] = new Restaurant ("Zack's Oak Bar & Restaurant", "232 Willow Avenue", false);
        restaurants [218] = new Restaurant ("Zafra", "301 Willow Avenue", false);
        restaurants [219] = new Restaurant ("Zena Grocery & Deli", "400 Jefferson Street", false);

        return restaurants;
    }
}


