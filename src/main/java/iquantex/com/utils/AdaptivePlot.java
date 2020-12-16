package iquantex.com.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static java.lang.Double.compare;
import static java.lang.Double.isFinite;

/**
 * @ClassName AdaptivePlot
 * @Description TODO
 * @Author jianping.mu
 * @Date 2020/12/7 3:44 下午
 * @Version 1.0
 */
public class AdaptivePlot {
    private final DoubleUnaryOperator f;
    private final double a;
    private final double c;
    private final SortedSet<Point> plot = new TreeSet<>((s, t) -> compare(s.x, t.x));

    public AdaptivePlot(DoubleUnaryOperator f, double a, double c) {
        this.f = f;
        this.a = a;
        this.c = c;
    }

    public static class Point {
        final double x, y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public AdaptivePlot computePlot(int depth, double eps) {
        plot.clear();
        Point pa = pointAt(a);
        Point pc = pointAt(c);
        plot.add(pa);
        plot.add(pc);
        computePlot(pa, pc, depth, eps);
        return this;
    }

    public List<Point> getPlot() {
        return new ArrayList<>(plot);
    }

    private Point pointAt(double x) {
        return new Point(x, f.applyAsDouble(x));
    }

    private void computePlot(Point pa, Point pc, int depth, double eps) {
        Point pb = pointAt(0.5 * (pa.x + pc.x));
        Point pa1 = pointAt(0.5 * (pa.x + pb.x));
        Point pb1 = pointAt(0.5 * (pb.x + pc.x));
        plot.add(pb);
        if (depth > 0 &&
                (oscillates(pa.y, pa1.y, pb.y, pb1.y, pc.y)
                        || unsmooth(pa.y, pa1.y, pb.y, pb1.y, pc.y, eps))) {
            computePlot(pa, pb, depth - 1, 2 * eps);
            computePlot(pb, pc, depth - 1, 2 * eps);
        }

        plot.add(pa1);
        plot.add(pb1);
    }

    private static boolean oscillates(
            double ya, double ya1, double yb, double yb1, double yc) {
        return isOscillation(ya, ya1, yb)
                && isOscillation(ya1, yb, yb1)
                && isOscillation(yb, yb1, yc);
    }

    private static boolean isOscillation(double ya, double yb, double yc) {
        return !isFinite(ya) || !isFinite(yb) || !isFinite(yc)
                || (yb > ya && yb > yc) || (yb < ya && yb < yc);
    }

    private static boolean unsmooth(
            double ya, double ya1, double yb, double yb1,double yc, double eps) {
        double y0 = DoubleStream.of(ya, ya1, yb, yb1, yc).min().getAsDouble();
        double [] yg = DoubleStream.of(ya, ya1, yb, yb1, yc).map(y -> y - y0).toArray();
        double q4 = quadrature(yg[0], yg[1], yg[2], yg[3]);
        double q3 = quadrature(yg[2], yg[3], yg[4]);
        return Math.abs(q4 - q3) > eps * q3;
    }

    private static double quadrature(double y0, double y1, double y2, double y3) {
        return 3d/8d * y0 + 19d/24d * y1 - 5d/24d * y2 + 1d/24d * y3;
    }

    private static double quadrature(double y0, double y1, double y2) {
        return 5d/12d * y0 + 2d/3d * y1 - 1d/12d * y2;
    }

    public static void main(String [] args) {
        List<Point> plot = new AdaptivePlot(x -> x * 2, 120d, 800d)
                .computePlot(-3, 0.005).getPlot();
        for (Point p : plot) {
            System.out.println(p.x + "\t" + p.y);
        }

        String s = "WQE,SDF";
        String sq = "WE,SDF";
        String sa = "QWE,WQE";
        ArrayList<String> objects = new ArrayList<>();
        ArrayList<String> objects1 = new ArrayList<>();
        objects1.add("asd");
        objects1.add("df");
        objects.addAll(objects1);
        objects.add(s.trim());
        objects.add(sa.trim());
        objects.add(sq.trim());
        System.out.println(objects);
        Map<String, Long> map1 = objects.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("moid出现次数统计（moid=次数）：" + map1);
        System.out.println(Objects.isNull(map1.get("fghndtyn")) ? "dfg" : map1.get("fghndtyn"));


        String s1 = "{\"tasks-57933\":{\"name\":\"ODSCH.TQ_BD_BIDINFO\",\"targetarr\":\"tasks-56787\",\"nodenumber\":\"1\",\"x\":201,\"y\":226},\"tasks-61888\":{\"name\":\"ODSCH.TQ_BD_CREDITRATE\",\"targetarr\":\"tasks-56787\",\"nodenumber\":\"1\",\"x\":223,\"y\":91},\"tasks-36671\":{\"name\":\"ODSCH.TQ_BD_OTCBASICINFO\",\"targetarr\":\"tasks-56787\",\"nodenumber\":\"1\",\"x\":225,\"y\":373},\"tasks-54481\":{\"name\":\"EINFO_CH.FI_BONDBID\",\"targetarr\":\"tasks-61888,tasks-36671\",\"nodenumber\":\"3\",\"x\":474,\"y\":188},\"tasks-75901\":{\"name\":\"EINFO_CH.FI_BASICINFO\",\"targetarr\":\"tasks-57933\",\"nodenumber\":\"1\",\"x\":457,\"y\":316},\"tasks-55695\":{\"name\":\"INFO_CH.FI_BONDBID\",\"targetarr\":\"tasks-54481\",\"nodenumber\":\"0\",\"x\":724,\"y\":203},\"tasks-50221\":{\"name\":\"INFO_CH.FI_BASICINFO\",\"targetarr\":\"tasks-75901,tasks-54481\",\"nodenumber\":\"0\",\"x\":732,\"y\":363},\"tasks-19160\":{\"name\":\"ods_einfo_info\",\"targetarr\":\"tasks-54481\",\"nodenumber\":\"0\",\"x\":740,\"y\":110},\"tasks-56787\":{\"name\":\"wes\",\"targetarr\":\"\",\"nodenumber\":\"3\",\"x\":48,\"y\":237}}";
        JSONObject o = JSON.parseObject(s1);
        for (String key:
             o.keySet()) {
            JSONObject jsonObject = o.getJSONObject(key);
            jsonObject.put("nodenumber","5");

        }
        System.out.println(o);
    }
}
