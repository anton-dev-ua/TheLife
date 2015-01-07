package thelife.engine.incubation;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

;

public class RleParser {
    public Set<Point> parse(String rlePattern) {

        Set<Point> points = new HashSet<>();

        Pattern positionPattern = Pattern.compile("Pos=(-?\\d),(-?\\d)");
        Matcher positionMatcher = positionPattern.matcher(rlePattern);
        if (positionMatcher.find()) {
//            System.out.println("position = " + positionMatcher.group(1) + " " + positionMatcher.group(2));
        }

        Pattern patternPattern = Pattern.compile("([bo\\d$]*)!");
        Matcher patternMatcher = patternPattern.matcher(rlePattern);
        if (patternMatcher.find()) {
//            System.out.println("pattern = " + patternMatcher.group(1));
        }

        int sx = Integer.valueOf(positionMatcher.group(1));
        int sy = Integer.valueOf(positionMatcher.group(2));


        String patternLine = patternMatcher.group(1);

        int x=sx,y=sy,number = 0;

        for (char ch : patternLine.toCharArray()) {
            if (Character.isDigit(ch)) {
                number = number * 10 + Integer.valueOf("" + ch);
            }
            if (ch == 'o') {
                if(number == 0 ) number = 1;
                for(int i=0;i<number; i++) {
                    points.add(new Point(x, y));
                    x++;
                }
                number = 0;
            }

            if(ch == 'b') {
                if(number == 0 ) number = 1;
                x+=number;
                number=0;
            }

            if(ch == '$') {
                if(number == 0 ) number = 1;
                y+=number;
                x=sx;
                number=0;
            }
        }

        return points;
    }
}

