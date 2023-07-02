package p1;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Chart extends JPanel {
    private List<String> mitre;
    private List<String> atomic;
    private double coverageRate;
    private Color atomicColor = Color.BLUE;
    private Color missingColor = Color.RED;

    public Chart(List<String> mitre, List<String> atomic) {
        this.mitre = mitre;
        this.atomic = atomic;
        int count = 0;
        for (String m : mitre) {
            for (String a : atomic) {
                if (m.equals(a)) {
                    count++;
                }
            }
        }
        coverageRate =  (float) count / mitre.size();
        setPreferredSize(new Dimension(300, 300));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height) - 20;
        int x = (width - diameter) / 2;
        int y = (height - diameter) / 2;

        // Draw the circle
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillOval(x, y, diameter, diameter);

        // Draw the atomic elements
        g2.setColor(atomicColor);
        double startAngle = Math.toDegrees(Math.atan2((double) height / 2 - y, (double) width / 2 - x)) + 90;
        double endAngle = startAngle + 360 * (coverageRate / 100.0);
        g2.fillArc(x, y, diameter, diameter, (int) startAngle, (int) (endAngle - startAngle));

        // Draw the missing elements
        g2.setColor(missingColor);
        Set<String> missing = new HashSet<>(mitre);
        missing.removeAll(atomic);
        startAngle = endAngle;
        endAngle = startAngle + 360 * (missing.size() / (double) mitre.size());
        g2.fillArc(x, y, diameter, diameter, (int) startAngle, (int) (endAngle - startAngle));

        // Draw the legend
        int legendWidth = width / 3;
        int legendHeight = height / 3;
        int legendX = width - legendWidth - 10;
        int legendY = 10;
        g2.setColor(Color.WHITE);
        g2.fillRect(legendX, legendY, legendWidth, legendHeight);
        g2.setColor(atomicColor);
        g2.fillOval(legendX + 10, legendY + 10, legendHeight - 20, legendHeight - 20);
        g2.setColor(Color.BLACK);
        g2.drawString("Atomic Elements", legendX + legendHeight, legendY + legendHeight / 2);
        g2.setColor(missingColor);
        g2.fillOval(legendX + 10, legendY + legendHeight + 10, legendHeight - 20, legendHeight - 20);
        g2.setColor(Color.BLACK);
        g2.drawString("Missing Elements", legendX + legendHeight, legendY + legendHeight + legendHeight / 2);

        // Draw the coverage rate text
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String rateString = String.format("%.2f%%", coverageRate);
        int textWidth = g2.getFontMetrics().stringWidth(rateString);
        int textHeight = g2.getFontMetrics().getHeight();
        g2.drawString(rateString, width / 2 - textWidth / 2, height / 2 + textHeight / 2);
    }

    public void setAtomicColor(Color color) {
        /// set default color is blue
        atomicColor = color;
        repaint();
    }

    public void setMissingColor(Color color) {
        missingColor = color;
        repaint();
    }

    public void drawChart(List<String> mitre, List<String> atomic) {

        JFrame frame = new JFrame("Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Chart chart = new Chart(mitre, atomic);

        JButton detailButton = new JButton("More Detail");
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame detailFrame = new JFrame("Detail");
                detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Create a table to display the covered and missing elements
                String[] columnNames = {"Covered ID", "Missing ID"};
                List<String> missing = new ArrayList<>(mitre);
                missing.removeAll(atomic);
                Object[][] data = new Object[atomic.size() + missing.size()][2];
                int i = 0;
                for (String a : atomic) {
                    data[i][0] = a;
                    if (mitre.contains(a)) {
                        data[i][1] = "";
                    } else {
                        data[i][1] = "Missing";
                    }
                    i++;
                }
                for (String m : missing) {
                    data[i][0] = "";
                    data[i][1] = m;
                    i++;
                }
                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                JTable table = new JTable(model);

                JScrollPane scrollPane = new JScrollPane(table);
                detailFrame.getContentPane().add(scrollPane);

                detailFrame.pack();
                detailFrame.setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(detailButton);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(chart, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}