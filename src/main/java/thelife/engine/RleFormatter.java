package thelife.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RleFormatter {
    public String format(Collection<Point> points) {
        List<Point> pointList = new ArrayList<>();
        pointList.addAll(points);

//        pointList.forEach(System.out::println);

//        System.out.println("-----------");

        pointList.sort((p1, p2) -> p1.getY() < p2.getY() || (p1.getY() == p2.getY() && p1.getX() < p2.getX()) ? -1 : 1);

//        pointList.forEach(System.out::println);

        int posX = pointList.stream().mapToInt(Point::getX).min().getAsInt();
        int posY = pointList.stream().mapToInt(Point::getY).min().getAsInt();

        StringBuffer res = new StringBuffer();
        res.append("Pos=").append(posX).append(",").append(posY).append(" ");


        int x = posX - 1, y = posY;
        char c = 'o';
        int n = 0;
        for (Point point : pointList) {
            if (point.getY() > y) {
                write(res, c, n);
                write(res, '$', point.getY() - y);
                c = 'o';
                n = 0;
                y = point.getY();
                x = posX - 1;
            }

            if (point.getX() > x + 1) {
                write(res, c, n);
                write(res, 'b', point.getX() - x - 1);
                x = point.getX();
                c = 'o';
                n = 1;
            } else {
                n++;
                x++;
            }

        }

        write(res, c, n);
        res.append("!");

        return res.toString();
    }

    private void write(StringBuffer res, char c, int n) {
        if (n > 0) {
            res.append("" + (n > 1 ? n : "") + c);
        }
    }
}
