package ex1.tests;

import ex1.src.WGraph_Algo;
import ex1.src.WGraph_DS;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WGraph_AlgoTest {
    @Test
    public void testInit(){
        weighted_graph g0 = new WGraph_DS();
        for(int i=0; i<5; i++) {
            g0.addNode(i);
        }
        weighted_graph newOne= new WGraph_DS();
        for(int i=8; i<5; i++) {
            g0.addNode(i);
        }

        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(g0);
        g.init(newOne);
        Assertions.assertEquals(newOne,g.getGraph());
    }

    @Test
    public void testGetG(){
        weighted_graph g0 = new WGraph_DS();
        for(int i=0; i<5; i++) {
            g0.addNode(i);
        }
        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(g0);
        Assertions.assertEquals(g0,g.getGraph());
    }
    @Test
    public void testGetNullG(){
        weighted_graph g0 = new WGraph_DS();
        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(g0);
        Assertions.assertEquals(g0,g.getGraph());
    }
    @Test
    public void testCopy(){
        weighted_graph g0 = new WGraph_DS();
        for(int i=0; i<5; i++) {
            g0.addNode(i);
        }
        g0.connect(1,2,0.5);
        g0.connect(3,4,8);
        g0.connect(0,1,3);

        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(g0);

        weighted_graph copiedG= g.copy();
        Assertions.assertEquals(g0.nodeSize(),copiedG.nodeSize());
        Assertions.assertEquals(g0.edgeSize(),copiedG.edgeSize());

        boolean ans= copiedG.hasEdge(1,2)&&
                copiedG.hasEdge(3,4)&&copiedG.hasEdge(0,1);
        Assertions.assertTrue(ans);
        Assertions.assertEquals(g0,copiedG);

        copiedG.removeNode(3);
        Assertions.assertNotEquals(g0,copiedG);

    }


    @Test
    public void testIsConnected(){
        //null graph
        weighted_graph g0 = new WGraph_DS();

        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(g0);

        Assertions.assertTrue(g.isConnected());

        //graph with one node
        g.getGraph().addNode(1);
        Assertions.assertTrue(g.isConnected());

        for(int i=0; i<5;i++){
            g.getGraph().addNode(i);
        }
        for (int i=0; i<5; i++){
            for(int j=0; j<5;j++){
                g.getGraph().connect(i,j,0);
            }
        }

        Assertions.assertTrue(g.isConnected());

        for (int i=0; i<5; i++){
            g.getGraph().removeEdge(0,i);
        }
        Assertions.assertFalse(g.isConnected());

    }

    @Test
    public void testShortestDist() {

        //distance between nodes in null graph--->path doesnt exist.
        weighted_graph g0 = new WGraph_DS();
        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(g0);
        Assertions.assertEquals(-1,g.shortestPathDist(1,2));

        //check distance between exist nodes without edges.--->path doesnt exist.
        for (int i = 0; i < 5; i++) {
            g.getGraph().addNode(i);
        }
        Assertions.assertEquals(-1,g.shortestPathDist(1,2));

        //check distance between src==dest-->should be 0
        Assertions.assertEquals(0,g.shortestPathDist(1,1));

        //shortest dist should be zero
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                g.getGraph().connect(i, j, 0);
        }

        Assertions.assertEquals(0,g.shortestPathDist(0,3));

        //check what should happen when there is unconnected Node--->path doesnt exist.
        g.getGraph().addNode(4);
        Assertions.assertEquals(-1,g.shortestPathDist(0,4));

        //check what happen when dest doesnt exist--->path doesnt exist
        Assertions.assertEquals(-1,g.shortestPathDist(1,6));

    }


    @Test
    public void testShortestPathList(){
        //check what happen in null graph
        weighted_graph g0 = new WGraph_DS();
        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(g0);
        Assertions.assertNull(g.shortestPath(1,1));

        //check the collection of path from one Node to himSelf
        for (int i=0;i<3;i++){
            g.getGraph().addNode(i);
        }
        boolean ans=g.getGraph().getNode(1).equals(g.shortestPath(1,1).get(0))
                && g.shortestPath(1,1).size()==1;
        Assertions.assertTrue(ans);

        //check the case of unconnected node--->should get null
        Assertions.assertNull(g.shortestPath(0,2));

        //check the algorithm on standard graph
        for (int i=3;i<5;i++){
            g.getGraph().addNode(i);
        }
        g.getGraph().connect(0,2,2.5);
        g.getGraph().connect(2,4,0.5);
        g.getGraph().connect(3,4,1);
        g.getGraph().connect(0,3,0);
        g.getGraph().connect(0,1,0.5);
        g.getGraph().connect(0,4,4);

        boolean ans2= g.shortestPath(0,4).contains(g.getGraph().getNode(0))
                && g.shortestPath(0,4).contains(g.getGraph().getNode(3))
                && g.shortestPath(0,4).contains(g.getGraph().getNode(4))
                && g.shortestPath(0,4).size()==3;

        Assertions.assertTrue(ans2);

    }


    @Test
    public void testSaveAndRead() throws IOException {
        weighted_graph g0 = new WGraph_DS();
        for(int i=0; i<5; i++) {
            g0.addNode(i);
        }
        g0.connect(0,2,2);
        g0.connect(1,3,0.5);

        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(g0);
        g.save("graph.txt");
        g.load("graph.txt");
        System.out.println("\n graph size "+ g.getGraph().nodeSize());
        System.out.println("\n Edge size "+ g.getGraph().edgeSize());
        weighted_graph newG= g.getGraph();
        Assertions.assertEquals(g0,newG);
    }

    @Test
    public void testSaveAndReadNull() throws IOException {
        weighted_graph g0 = new WGraph_DS();
        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(g0);
        String s="graph.txt";
        Assertions.assertTrue(g.save(s));

        weighted_graph g1 = new WGraph_DS();
        Assertions.assertTrue(g.load(s));

        Assertions.assertEquals(g.getGraph(),g1);

        g.getGraph().addNode(1);
        Assertions.assertNotEquals(g.getGraph(),g1);


    }
    @Test
    public void testReadAndSave() throws IOException {
        weighted_graph g0 = new WGraph_DS();
        WGraph_DSTest.createGraph(g0,15);
        WGraph_DSTest.createEdgeForNode(g0,2,3);

        weighted_graph_algorithms g=new WGraph_Algo();
        g.init(g0);
        Assertions.assertEquals(g.getGraph(),g0);

        g.save("graph.txt");

        weighted_graph g1 = new WGraph_DS();
        WGraph_DSTest.createGraph(g1,15);
        WGraph_DSTest.createEdgeForNode(g1,2,3);

        g.load("graph.txt");

       Assertions.assertEquals(g0,g1);
       g1.connect(1,2,0.3);
        Assertions.assertNotEquals(g0,g1);



    }
}
