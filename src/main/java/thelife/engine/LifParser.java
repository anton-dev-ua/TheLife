package thelife.engine;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class LifParser {

    public Collection<Point> parse(String content) {
        String lifString = content.toUpperCase().replaceAll("O|o", "*");
        Collection<Point> points = new HashSet<>();
        int x = 0, y = 0;
        for (String line : lifString.split("\n")) {
            x = -1;
            y--;
            for (char c : line.trim().toCharArray()) {
                x++;
                if (c == '*') points.add(new Point(x, y));
            }
        }

        Point deltaPoint = new Point(-(x + 1) / 2, -(y - 1) / 2);
        return points.stream().map(point -> point.add(deltaPoint)).collect(Collectors.toSet());
    }
}
