package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class WGraph_DSTest {


    public static void createGraph(weighted_graph g, int graphSize){

        for (int i=0; i<graphSize; i++){
            g.addNode(i);
        }

    }

    public static void createEdgeForNode(weighted_graph g,int nodeKey, int edgeSize){
        for (int i=0; i<=edgeSize; i++){
            if(i==nodeKey)
                g.connect(nodeKey, ++i,0);
            g.connect(nodeKey,i,0);
        }
    }
    @Test
    public void testGetNode(){
        weighted_graph G= new WGraph_DS();
        Assertions.assertNull(G.getNode(9));

        G.addNode(8);
        Assertions.assertNotNull(G.getNode(8));
    }

    @Test
    public void testHasEdge(){
        weighted_graph g0 = new WGraph_DS();
        createGraph(g0,20);
        g0.connect(1,8,3);
        Assertions.assertTrue(g0.hasEdge(1,8));

        Assertions.assertFalse(g0.hasEdge(4,5));
    }

    @Test
    public void testGetEdge(){
        weighted_graph g0 = new WGraph_DS();
        createGraph(g0,20);

        //edge doesnt exist---> should return -1
        Assertions.assertEquals(g0.getEdge(0,5),-1);

        g0.connect(1,2,0.5);
        Assertions.assertEquals(g0.getEdge(1,2),0.5);

    }


    @Test
    public void testAddNode(){
        weighted_graph g0 = new WGraph_DS();
        g0.addNode(9);

        Assertions.assertTrue(g0.getV().contains(g0.getNode(9)));

        //node already exist --> mc should be the same
        int beforeMc=g0.getMC();
        g0.addNode(9);
        int afterMc=g0.getMC();

        Assertions.assertEquals(beforeMc,afterMc);
    }

    @Test
    public void testConnect(){
        weighted_graph g0 = new WGraph_DS();
        createGraph(g0,30);
        createEdgeForNode(g0,0,8);

        Assertions.assertTrue(g0.hasEdge(0,1));

        Assertions.assertFalse(g0.hasEdge(5,4));

        g0.connect(5,4,0);
        Assertions.assertTrue(g0.hasEdge(5,4));

        g0.connect(5,4,2.5);
        Assertions.assertEquals(2.5, g0.getEdge(5,4));

        //edge already exist--> mc should be the same.
        int beforeMc=g0.getMC();
        g0.connect(5,4,2.5);
        int afterMc=g0.getMC();
        Assertions.assertEquals(beforeMc,afterMc);
    }


    @Test
    public void testGetV(){
        //Null graph
        weighted_graph g0 = new WGraph_DS();
        Assertions.assertEquals(g0.getV().size(),0);

        Collection<node_info> v = g0.getV();
        Iterator<node_info> iter = v.iterator();
        Assertions.assertFalse(iter.hasNext());

        //the graph isn't null.
        createGraph(g0,10);

        Collection<node_info> v1 = g0.getV();
        Iterator<node_info> iter1 = v1.iterator();
        while (iter.hasNext()) {
            node_info n = iter.next();
            Assertions.assertNotNull(n);
        }

        Assertions.assertEquals(10, g0.getV().size());
    }

    @Test
    public void testGetVOfNode(){
        weighted_graph g0 = new WGraph_DS();
        createGraph(g0,10);
        createEdgeForNode(g0,1,5);

        Collection<node_info> v = g0.getV(1);
        Iterator<node_info> iter = v.iterator();
        while (iter.hasNext()) {
            node_info n = iter.next();
            Assertions.assertNotNull(n);
        }

        Assertions.assertEquals(5, g0.getV(1).size());
    }

    @Test

    public void testRemoveNode() {
        //check if the node exist at the graph after we delete him
        weighted_graph g0 = new WGraph_DS();
        createGraph(g0,10);
        g0.connect(1,2,0.5);

        g0.removeNode(1);

        Assertions.assertNull(g0.getNode(1));
        assertEquals(g0.getNode(1),g0.removeNode(1) );

        //check the node edges
        Assertions.assertFalse(g0.hasEdge(1,2));
        Assertions.assertFalse(g0.hasEdge(2,1));

        //delete node doesnt exist
        Assertions.assertNull(g0.removeNode(20));
    }


    @Test

    public void testRemoveEdge(){
        weighted_graph g0 = new WGraph_DS();
        createGraph(g0,10);
        g0.connect(1,2,0);

        g0.removeEdge(1,2);
        Assertions.assertFalse(g0.hasEdge(1,2));
        Assertions.assertFalse(g0.hasEdge(2,1));

        //remove edge doesnt exist--> mc should be the same.
        int beforeMc=g0.getMC();
        g0.removeEdge(0,1);
        int afterMc=g0.getMC();
        Assertions.assertEquals(beforeMc,afterMc);
    }


    @Test
    public void testNodeSize(){
        weighted_graph g0 = new WGraph_DS();
        createGraph(g0,10);

        Assertions.assertEquals(10,g0.nodeSize());

        g0.removeNode(0);
        Assertions.assertEquals(9,g0.nodeSize());

        //remove node doesnt exist--> nodeSize should be the same.
        g0.removeNode(0);
        Assertions.assertEquals(9,g0.nodeSize());

        for (int i=0; i<10; i++)
            g0.removeNode(i);

        Assertions.assertEquals(0,g0.nodeSize());
    }

    @Test
    public void testEdgeSize(){
        weighted_graph g0 = new WGraph_DS();
        //null graph
        Assertions.assertEquals(0,g0.edgeSize());

        createGraph(g0,20);
        createEdgeForNode(g0,1,5);

        Assertions.assertEquals(5,g0.edgeSize());

        g0.removeEdge(1,2);
        Assertions.assertEquals(4,g0.edgeSize());

        //delete the same edge--> edgeSize should be the same.
        g0.removeEdge(1,2);
        Assertions.assertEquals(4,g0.edgeSize());

        //connect exist edge
        g0.connect(1,3,0);
        Assertions.assertEquals(4,g0.edgeSize());

        //edgeSize after remove node
        //create 10 edges
        createEdgeForNode(g0,2,10);
        Assertions.assertEquals(14,g0.edgeSize());

        //delete 5 edges
        g0.removeNode(1);

        Assertions.assertEquals(9,g0.edgeSize());

    }

    @Test
    public void testGetMcAfterBuildG(){
        weighted_graph g0 = new WGraph_DS();
        createGraph(g0,20);
        Assertions.assertEquals(20,g0.getMC());

        createEdgeForNode(g0,1,5);
        Assertions.assertEquals(25,g0.getMC());

        createEdgeForNode(g0,2,10);
        Assertions.assertEquals(34,g0.getMC());

    }

    @Test
    public void testMc(){
        weighted_graph g0 = new WGraph_DS();
        createGraph(g0,20);
        Assertions.assertEquals(20,g0.getMC());

        g0.addNode(1);
        Assertions.assertEquals(20,g0.getMC());

        for(int i=5; i<10;i++){
            g0.connect(1,i,0);
        }
        Assertions.assertEquals(25,g0.getMC());

        g0.removeNode(1);
        Assertions.assertEquals(31,g0.getMC());
    }


    @Test
    //10^6 nodes, 10^7 edges.
    public void testTimeRun(){
        long start= new Date().getTime();
        weighted_graph g0 = new WGraph_DS();
        for (Integer i=0 ; i<=Math.pow(10,6) ; i++){
            g0.addNode(i);
        }
        for (Integer i=0; i<=10; i++){
            for(Integer j=i; j<Math.pow(10,6); j++) {
                g0.connect(i, j, 0.5);
            }
        }
        long end= new Date().getTime();
        double dt= (end-start)/1000.0;
        System.out.println(dt+" seconds");

    }
}
