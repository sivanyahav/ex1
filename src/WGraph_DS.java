package ex1.src;

import java.util.*;

public class WGraph_DS implements weighted_graph {

    private HashMap<Integer,HashMap<Integer,Double>> neighbors;
    private HashMap<Integer, node_info> nodes;
    private int countEdge;
    private int mc;

    public WGraph_DS(){
        nodes=new HashMap<>();
        neighbors=new HashMap<>();
        countEdge=0;
        mc=0;
    }
    //*************** private class of node ***************//
    private static class NodeInfo implements node_info{
        private int key;
        private String info;
        private double tag;

        public NodeInfo(int key){
            info=null;
            tag=0;
            this.key=key;
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info=s;
        }

        @Override
        public double getTag() {
            return this.tag;
        }

        @Override
        public void setTag(double t) {
            this.tag=t;
        }

        public String toString ()
        {
            String s = "The node key: " + key;
            s+=" , the tag is: "+tag+" , the info: "+info+"\n";

            return s;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return this.toString().equals(nodeInfo.toString());
        }

    }

    @Override
    //this function return the node_data by the node key.
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    @Override
    //this function return true if and only if
    // there is an edge between node1 and node2

    public boolean hasEdge(int node1, int node2) {
        if(neighbors.containsKey(node1))
            return neighbors.get(node1).containsKey(node2);
        return false;
    }

    @Override
    //this function return the weight of the edge (node1, node1).
    // In case there is no such edge - return -1

    public double getEdge(int node1, int node2) {

        if(hasEdge(node1,node2))
            return neighbors.get(node1).get(node2);
        return -1;
    }

    @Override
    //this function add a new node to the graph with the given key.
    //if there is already a node with such a key there is no action.

    public void addNode(int key) {
        if(!nodes.containsKey(key)) {
            mc++;
            nodes.put(key, new NodeInfo(key));
            HashMap<Integer, Double> NiMap = new HashMap<>();
            neighbors.put(key, NiMap);
        }
    }

    @Override
    //this function connect an edge between node1 and node2,
    // with an edge with weight >=0.
    //if the edge node1-node2 already exists - the method simply updates the weight of the edge.

    public void connect(int node1, int node2, double w) {
        if(nodes.containsKey(node1)&&nodes.containsKey(node2)) {
            if (node1 != node2) {
                if(neighbors.get(node1).containsKey(node2)
                        && neighbors.get(node1).get(node2)==w)
                    return;

                if(!neighbors.get(node1).containsKey(node2))
                    countEdge++;

                mc++;

                neighbors.get(node1).put(node2, w);
                neighbors.get(node2).put(node1, w);

            }
        }
    }

    @Override
    // this function return a pointer (shallow copy) for a
    // collection representing all the nodes in the graph.

    public Collection<node_info> getV() {
        return nodes.values();
    }

    @Override
    //this function returns a Collection containing all the
    //nodes connected to node_id by passing all the nodes neighbors.

    public Collection<node_info> getV(int node_id) {

        ArrayList<node_info> ans=new ArrayList<>();

        if (nodes.containsKey(node_id)) {
            Iterator<Integer> it= neighbors.get(node_id).keySet().iterator();
            while(it.hasNext()) {
                Integer neighborKey=it.next();
                node_info newOne = getNode(neighborKey);
                ans.add(newOne);
            }
        }
        return ans;
    }

    @Override
    // this function delete the node (with the given ID) from the graph -
    // and removes all edges which starts or ends at this node.

    public node_info removeNode(int key) {
        if(nodes.containsKey(key)) {
            mc++;

            node_info ans= getNode(key);
            Iterator<Integer> it= neighbors.get(key).keySet().iterator();
            //passing the nodes neighbors and remove the given node
            //we are delete edges, so we need to reduce the number of edges
            // and increase the number of actions
            while(it.hasNext()){
                Integer niKey=it.next();
                neighbors.get(niKey).remove(key);
                countEdge--;
                mc++;

            }
            neighbors.remove(key);
            nodes.remove(key);
            return ans;
        }
        return null;
    }


    @Override
    //this function delete the edge from the graph,
    //if the edge doesnt exist there is no action.

    public void removeEdge(int node1, int node2) {
        if(nodes.containsKey(node1)&&nodes.containsKey(node2)) {
            if(node1 != node2 && neighbors.get(node1).containsKey(node2)) {
                neighbors.get(node1).remove(node2);
                neighbors.get(node2).remove(node1);
                countEdge--;
                mc++;
            }
        }
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return countEdge;
    }

    @Override
    public int getMC() {
        return mc;
    }

    public String toString()
    {
        String s = "";
        for(node_info node: this.getV()){
            s += node.toString();
            for (node_info ni : this.getV(node.getKey())) {

                s += " the neighbors are: " +ni.getKey()+" the edge weight: "
                        +this.getEdge(node.getKey(),ni.getKey())+",\n";

            }
            s+="\n";
        }
        s+="the mc is: "+this.getMC()+"\n";

        return s;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return this.toString().equals(wGraph_ds.toString());
    }


}
