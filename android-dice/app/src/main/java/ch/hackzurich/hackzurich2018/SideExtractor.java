package ch.hackzurich.hackzurich2018;

public class SideExtractor {

    public int extract(float x, float y, float z) {
        float absX = Math.abs(x);
        float absY = Math.abs(y);
        float absZ = Math.abs(z);
        float maxValue = Math.max(absX, Math.max(absY, absZ));

        if (maxValue == absX) {
            if (x > 0) {
                return 4;
            } else {
                return 1;
            }
        } else if (maxValue == absY) {
            if (y > 0) {
                return 2;
            } else {
                return 3;
            }
        } else {
            if (z > 0) {
                return 0;
            } else {
                return 5;
            }
        }
    }

}
