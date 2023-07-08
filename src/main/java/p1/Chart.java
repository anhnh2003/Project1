package p1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
public class Chart extends JComponent implements MouseListener {
    
    private List<String> mitre;
    private List<String> atomic;
    private Set<String> mitreNotAtomic;
    private int bothCount;
    private int mitreNotAtomicCount;
    private static final int CHART_SIZE = 300;
    private static final int CHART_MARGIN = 200;
    private static final int ARC_START_ANGLE = 90; // Start angle for both and mitre not atomic arcs
    private static final int ARC_GAP_ANGLE = 0; // Gap between arcs to show separation
    private static final Color BOTH_COLOR = Color.BLUE;
    private static final Color MITRE_NOT_ATOMIC_COLOR = Color.RED;
    
    public Chart(List<String> mitre, List<String> atomic) {
        this.mitre = mitre;
        this.atomic = atomic;
        this.bothCount = 0;
        this.mitreNotAtomicCount = 0;
        this.mitreNotAtomic = new HashSet<>();
        
        // Calculate counts and populate mitreNotAtomic set
        for (String item : mitre) {
            if (atomic.contains(item)) {
                bothCount++;
            } else {
                mitreNotAtomicCount++;
                mitreNotAtomic.add(item);
            }
        }
        // Set component size to square, with padding
        setPreferredSize(new Dimension(CHART_SIZE + CHART_MARGIN * 2, CHART_SIZE + CHART_MARGIN * 2));
        
        // Add mouse listener to track mouse events
        addMouseListener(this);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Calculate angles and percentages for both and mitre not atomic arcs
        int totalAngle = 360 - ARC_GAP_ANGLE; // Total angle available, accounting for gap
        int bothAngle = bothCount * totalAngle / mitre.size();
        int mitreNotAtomicAngle = mitreNotAtomicCount * totalAngle / mitre.size();
        double bothPercentage = (double) bothAngle / 360 * 100;
        double mitreNotAtomicPercentage = (double) mitreNotAtomicAngle / 360 * 100;
        
        // Adjust angles if necessary to ensure sum is exactly 360 degrees
        int angleSum = bothAngle + mitreNotAtomicAngle;
        if (angleSum < totalAngle) {
            mitreNotAtomicAngle += totalAngle - angleSum;
            mitreNotAtomicPercentage = (double) mitreNotAtomicAngle / 360 * 100;
        } else if (angleSum > totalAngle) {
            bothAngle += angleSum - totalAngle;
            bothPercentage = (double) bothAngle / 360 * 100;
        }
        
        // Draw both arc
        g.setColor(BOTH_COLOR);
        // Center of chart position
        final int CENTER_MARGIN_X = CHART_MARGIN + 300;
        final int CENTER_MARGIN_Y = CHART_MARGIN;
        g.fillArc(CENTER_MARGIN_X,CENTER_MARGIN_Y, CHART_SIZE, CHART_SIZE, ARC_START_ANGLE, bothAngle);
        int radius = CHART_SIZE / 2;
        // Set label position
        int bothLabelX = (CENTER_MARGIN_X+20);
        int bothLabelY = (CENTER_MARGIN_Y+120);
        // Draw mitre not atomic arc 
        g.setColor(MITRE_NOT_ATOMIC_COLOR);
        g.fillArc(CENTER_MARGIN_X, CENTER_MARGIN_Y, CHART_SIZE, CHART_SIZE, ARC_START_ANGLE + bothAngle + ARC_GAP_ANGLE, mitreNotAtomicAngle);
        int mitreNotAtomicLabelX = (CENTER_MARGIN_X+220);
        int mitreNotAtomicLabelY = (CENTER_MARGIN_Y+120);
        // Draw percentage labels
        g.setColor(Color.BLACK);
        String bothLabel = String.format("%.2f%%", bothPercentage);
        String mitreNotAtomicLabel = String.format("%.2f%%", mitreNotAtomicPercentage);
        g.drawString(bothLabel, bothLabelX, bothLabelY);
        g.drawString(mitreNotAtomicLabel, mitreNotAtomicLabelX, mitreNotAtomicLabelY);
        
        // Draw chart label
        g.setColor(Color.BLACK);
        g.drawString("Mitre vs. Atomic Techniques", CENTER_MARGIN_X+50, CENTER_MARGIN_Y-20);
        drawTable((Graphics2D) g, 50, 50, getWidth() - 100, getHeight() - 100);
        // draw description
        g.setColor(BOTH_COLOR);
        g.fillRect(CENTER_MARGIN_X, CENTER_MARGIN_Y+2*radius+20, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("Covered Techniques", CENTER_MARGIN_X+20, CENTER_MARGIN_Y+2*radius+35);
        g.setColor(MITRE_NOT_ATOMIC_COLOR);
        g.fillRect(CENTER_MARGIN_X+150, CENTER_MARGIN_Y+2*radius+20, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("Missing Techniques", CENTER_MARGIN_X+170, CENTER_MARGIN_Y+2*radius+35);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        // Determine which arc was clicked
        int x = e.getX() - CHART_MARGIN-300;
        int y = e.getY() - CHART_MARGIN;
        int radius = CHART_SIZE / 2;
        int centerX = radius;
        int centerY = radius;
        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        
        if (distance > radius) {
            // Clicked outside chart, do nothing
            return;
        }
        
        // Determine which set of items to display
        List<String> items;
        if (x < centerX) {
            items = atomic;
        } else {
            items = new ArrayList<>(mitreNotAtomic);
        }
        
        // Display items
        JFrame frame = new JFrame();
        String title = (x < centerX ? "Covered Techniques" : "Missing Techniques");
        StringBuilder message = new StringBuilder();
        message.append("Total: ").append(items.size()).append("Techniques").append("\n");
        for (String item : items) {
            message.append(item).append("\n");
        }
        frame.setTitle(title);
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(this);
        JTextArea textArea = new JTextArea(message.toString());
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea));
        frame.setVisible(true);
        
    }
    private void drawTable(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(Color.BLACK);
        int rowHeight = 20;
        int headerY = y + rowHeight;
        g2d.drawLine(x, headerY, x + width, headerY);
        g2d.drawString("Mitre techniques", x + 5, headerY + rowHeight - 5);
        g2d.drawString("Status", x + 205, headerY + rowHeight - 5);
        int itemY = headerY + rowHeight;
        for (String item : mitre) {
            if (mitreNotAtomic.contains(item)) {
                g2d.drawString(item, x + 5, itemY + rowHeight - 5);
                g2d.drawString("Missing", x + 205, itemY + rowHeight - 5);
            } else {
                g2d.drawString(item, x + 5, itemY + rowHeight - 5);
            }
            itemY += rowHeight;
        }
    }
    // Unused mouse listener methods
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    
    public void drawChart(){
        JFrame frame = new JFrame();
        frame.setTitle("Coverage Chart");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Add the chart to the main panel
        mainPanel.add(this, BorderLayout.CENTER);

        // Add the 'more details' button to a separate panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton detailButton = new JButton("More Detail");
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame detailFrame = new JFrame("Detail");
                detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Create a table to display the covered and missing elements
                String[] columnNames = {"Covered ID", "Missing ID"};
                Object[][] data = new Object[atomic.size() + mitreNotAtomic.size()][2];
                int i = 0;
                for(String item: atomic) {
                    data[i][0] = item;
                    i++;
                }
                i = 0;
                for(String item: mitreNotAtomic) {
                    data[i][1] = item;
                    i++;
                }
                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                JTable table = new JTable(model){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                JScrollPane scrollPane = new JScrollPane(table);
                detailFrame.getContentPane().add(scrollPane);

                detailFrame.pack();
                detailFrame.setVisible(true);
            }
        });
        buttonPanel.add(detailButton);

        // Add the button panel to the main panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
}