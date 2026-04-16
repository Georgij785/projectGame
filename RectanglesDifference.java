import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class RectanglesDifference extends JPanel {

    // Множества прямоугольников
    private final List<Rectangle> firstSet = new ArrayList<>();
    private final List<Rectangle> secondSet = new ArrayList<>();

    // Цвета
    private static final Color FIRST_SET_COLOR = Color.RED;
    private static final Color SECOND_SET_COLOR = Color.BLUE;
    private static final Color DIFFERENCE_COLOR = new Color(0, 255, 0, 128);
    private static final Color PREVIEW_COLOR = Color.GRAY;

    // Поля ввода
    private final JTextField xField = new JTextField(5);
    private final JTextField yField = new JTextField(5);
    private final JTextField widthField = new JTextField(5);
    private final JTextField heightField = new JTextField(5);

    // Выбор множества для добавления
    private final JRadioButton firstSetRadio = new JRadioButton("Первое множество", true);
    private final JRadioButton secondSetRadio = new JRadioButton("Второе множество");

    // Координаты мыши для отображения
    private final JLabel mouseCoordLabel = new JLabel("X: 0, Y: 0");

    // Переменные для рисования мышью
    private Point startPoint = null;
    private Point currentPoint = null;
    private boolean isDrawing = false;

    // Панель для рисования
    private final JPanel drawingPanel;

    public RectanglesDifference() {
        setLayout(new BorderLayout());

        // Создаём панель рисования с обработчиками мыши
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawContent((Graphics2D) g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 600);
            }
        };
        drawingPanel.setBackground(Color.WHITE);
        setupMouseListeners();
        add(drawingPanel, BorderLayout.CENTER);

        // Панель управления
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);
    }


    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                currentPoint = startPoint;
                isDrawing = true;
                drawingPanel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                currentPoint = e.getPoint();
                drawingPanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (startPoint != null && currentPoint != null) {
                    Rectangle rect = createRectFromPoints(startPoint, currentPoint);
                    if (rect.width > 0 && rect.height > 0) {
                        if (firstSetRadio.isSelected()) {
                            firstSet.add(rect);
                        } else {
                            secondSet.add(rect);
                        }
                    }
                }
                startPoint = null;
                currentPoint = null;
                isDrawing = false;
                drawingPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseCoordLabel.setText(String.format("X: %d, Y: %d", e.getX(), e.getY()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseCoordLabel.setText("X: ---, Y: ---");
            }
        };

        drawingPanel.addMouseListener(mouseAdapter);
        drawingPanel.addMouseMotionListener(mouseAdapter);
    }


    private Rectangle createRectFromPoints(Point p1, Point p2) {
        int x = Math.min(p1.x, p2.x);
        int y = Math.min(p1.y, p2.y);
        int width = Math.abs(p1.x - p2.x);
        int height = Math.abs(p1.y - p2.y);
        return new Rectangle(x, y, width, height);
    }


    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Поля числового ввода
        panel.add(new JLabel("X:"));
        panel.add(xField);
        panel.add(new JLabel("Y:"));
        panel.add(yField);
        panel.add(new JLabel("Ширина:"));
        panel.add(widthField);
        panel.add(new JLabel("Высота:"));
        panel.add(heightField);

        JButton addToFirstBtn = new JButton("Добавить в первое");
        addToFirstBtn.addActionListener(e -> addFromFields(true));
        panel.add(addToFirstBtn);

        JButton addToSecondBtn = new JButton("Добавить во второе");
        addToSecondBtn.addActionListener(e -> addFromFields(false));
        panel.add(addToSecondBtn);

        panel.add(new JSeparator(SwingConstants.VERTICAL));


        ButtonGroup group = new ButtonGroup();
        group.add(firstSetRadio);
        group.add(secondSetRadio);
        panel.add(firstSetRadio);
        panel.add(secondSetRadio);

        panel.add(new JSeparator(SwingConstants.VERTICAL));

        JButton clearFirstBtn = new JButton("Очистить первое");
        clearFirstBtn.addActionListener(e -> {
            firstSet.clear();
            repaint();
        });
        panel.add(clearFirstBtn);

        JButton clearSecondBtn = new JButton("Очистить второе");
        clearSecondBtn.addActionListener(e -> {
            secondSet.clear();
            repaint();
        });
        panel.add(clearSecondBtn);

        JButton clearAllBtn = new JButton("Очистить всё");
        clearAllBtn.addActionListener(e -> {
            firstSet.clear();
            secondSet.clear();
            repaint();
        });
        panel.add(clearAllBtn);

        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(mouseCoordLabel);

        return panel;
    }


    private void addFromFields(boolean toFirstSet) {
        Rectangle rect = parseRectangle();
        if (rect != null) {
            if (toFirstSet) {
                firstSet.add(rect);
            } else {
                secondSet.add(rect);
            }
            repaint();
        }
    }

    private Rectangle parseRectangle() {
        try {
            int x = Integer.parseInt(xField.getText().trim());
            int y = Integer.parseInt(yField.getText().trim());
            int width = Integer.parseInt(widthField.getText().trim());
            int height = Integer.parseInt(heightField.getText().trim());

            if (width <= 0 || height <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Ширина и высота должны быть положительными.",
                        "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return new Rectangle(x, y, width, height);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Введите целые числа во все поля.",
                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }


    private void drawContent(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(DIFFERENCE_COLOR);
        int width = drawingPanel.getWidth();
        int height = drawingPanel.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isInFirstSet(x, y) && !isInSecondSet(x, y)) {
                    g2d.fillRect(x, y, 1, 1);
                }
            }
        }


        g2d.setColor(FIRST_SET_COLOR);
        g2d.setStroke(new BasicStroke(2));
        for (Rectangle rect : firstSet) {
            g2d.draw(rect);
        }

        g2d.setColor(SECOND_SET_COLOR);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                10, new float[]{5, 5}, 0));
        for (Rectangle rect : secondSet) {
            g2d.draw(rect);
        }


        if (isDrawing && startPoint != null && currentPoint != null) {
            Rectangle preview = createRectFromPoints(startPoint, currentPoint);
            g2d.setColor(PREVIEW_COLOR);
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10, new float[]{3, 3}, 0));
            g2d.draw(preview);
        }


        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Красный контур — первое множество", 20, 20);
        g2d.drawString("Синий пунктир — второе множество", 20, 40);
        g2d.drawString("Зелёная заливка — разность (первое \\ второе)", 20, 60);
        g2d.drawString("Для рисования мышью: зажмите кнопку и растяните прямоугольник", 20, 80);
    }

    private boolean isInFirstSet(int x, int y) {
        for (Rectangle rect : firstSet) {
            if (rect.contains(x, y)) return true;
        }
        return false;
    }

    private boolean isInSecondSet(int x, int y) {
        for (Rectangle rect : secondSet) {
            if (rect.contains(x, y)) return true;
        }
        return false;
    }

    @Override
    public void repaint() {
        if (drawingPanel != null) {
            drawingPanel.repaint();
        }
        super.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Разность множеств прямоугольников");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new RectanglesDifference());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}