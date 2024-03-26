package Hill_Climbing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

public class Graph {
	static class Node {
	    String name;
	    int priority;

	    public Node(String name, int priority) {
	        this.name = name;
	        this.priority = priority;
	    }

	    @Override
	    public String toString() {
	        return name;
	    }
	}
	
	public static void main(String[] args) {
		Map<String, Integer> trongSo = readVertices("src/Hill_Climbing/input.txt");
        Map<String, List<String>> danhSach = readEdges("src/Hill_Climbing/input.txt");
        if (danhSach != null) {
            String res = hillClimbing(danhSach, trongSo, "A", "B");
            if (res != null) {
                printFile("src/DFS/output.txt", res);
                System.out.println(res);
            } else {
                System.out.println("Không tìm thấy đường đi.");
            }
        } else {
            System.out.println("Đã xảy ra lỗi khi đọc file input.txt.");
        }
	}
	
	public static String hillClimbing(Map<String, List<String>> danhSach, Map<String, Integer> trongSo, String start, String end) {
        if (!danhSach.containsKey(start) || !danhSach.containsKey(end)) {
            return null;
        }

        Stack<Node> stack = new Stack<>();
        stack.push(new Node(start, trongSo.get(start)));
        List<Node> L1 = new ArrayList<>();
        List<Node> L = new ArrayList<>();
        L.add(new Node(start, trongSo.get(start)));
        List<String> Ke = new ArrayList<>();
        List<String[]> row = new ArrayList<>();
        //Map<String, String> visited = new HashMap<>();
      
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            L.removeIf(n -> n.name.equals(node.name));
            Ke.clear();
            List<String> dinhKeList = danhSach.get(node);
            if (dinhKeList != null) {
                for (String dinhKe : dinhKeList) {
                    Ke.add(dinhKe);
                    if (!dinhKe.equals(start) /*&& !visited.containsKey(dinhKe)*/) {
                        L1.add(new Node(dinhKe, trongSo.get(dinhKe)));
                        //visited.put(dinhKe, node.name);
                        
                    }
                }
                L1.sort(Comparator.comparingInt(n -> n.priority));
                for(Node n: L1)
                	L.add(n);
            }           
          
            if (node.equals(end)) {
                List<String> duongDi = new ArrayList<>();
                duongDi.add(node.name);
                //while (!duongDi.get(duongDi.size() - 1).equals(start)) {
                //    duongDi.add(farther.get(duongDi.get(duongDi.size() - 1)));
                //}
                row.add(new String[] { node.name, "Tìm thấy-TTKT Dừng", null, null });
                StringBuilder result = new StringBuilder();
                for (int i = duongDi.size() - 1; i >= 0; i--) {
                    result.append(duongDi.get(i));
                    if (i != 0) {
                        result.append(" => ");
                    }
                }
                return tabulate(row, new String[] { "Phát triển TT", "Trạng thái kề", "Danh sách Q", "Danh sách L" })
                        + "\nĐường đi là: " + result.toString();
            }
            row.add(new String[] { node.name, String.join(", ", Ke), String.join(", ", L1.toString()), String.join(", ", L.toString()) });
        }
      
        return null;
    }
	
	public static Map<String, Integer> readVertices(String filename) {
        Map<String, Integer> vertices = new HashMap<>();
        try (BufferedReader file = new BufferedReader(new FileReader(filename))) {
            String line = file.readLine();
            while (line != null && !line.trim().isEmpty()) {
                String[] temp = line.split(" ");
                String vertex = temp[0];
                int value = Integer.parseInt(temp[1]);
                vertices.put(vertex, value);
                line = file.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vertices;
    }

    public static Map<String, List<String>> readEdges(String filename) {
        Map<String, List<String>> edges = new HashMap<>();
        try (BufferedReader file = new BufferedReader(new FileReader(filename))) {
            String line = file.readLine();
            while (line != null && !line.trim().isEmpty()) {
                line = file.readLine();
            }
            line = file.readLine(); // Đọc dòng trống
            while (line != null) {
                if (!line.trim().isEmpty()) {
                    String[] temp = line.split(" ");
                    String vertex1 = temp[0];
                    String vertex2 = temp[1];
                    edges.putIfAbsent(vertex1, new ArrayList<>());
                    edges.get(vertex1).add(vertex2);
                }
                line = file.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return edges;
    }
    
    public static void printFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String tabulate(List<String[]> rows, String[] headers) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(" | ", headers)).append("\n");
        for (String[] row : rows) {
            sb.append(String.join(" | ", row)).append("\n");
        }
        return sb.toString();
    }
}
