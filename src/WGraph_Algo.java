package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {
    private weighted_graph _g;

    public WGraph_Algo(){

    }
    @Override
    //this method init the graph on which this set of algorithms operates on.
    public void init(weighted_graph g) {
        _g= g;
    }

    @Override
    //this method return the underlying graph of which this class works.
    public weighted_graph getGraph() {
        return _g;
    }

    @Override
    //this method compute a deep copy of this weighted graph.
    //we use two private function.

    public weighted_graph copy() {
        weighted_graph ans= new WGraph_DS();
        copyNodes(ans);
        copyEdges(ans);
        return ans;
    }
    //this function copy the nodes from our graph to the other graph.
    private void copyNodes(weighted_graph g){
        //passing the graph's nodes.
        for (node_info n: _g.getV()){
            Integer newOne= n.getKey();
            g.addNode(newOne);
        }
    }
    //this function copy the edges from our graph to the other graph.
    private void copyEdges(weighted_graph g){
        //passing the graph's nodes.
        for (node_info node: _g.getV()){
            //passing the node's neighbors.
            for(node_info ni: _g.getV(node.getKey())){
                //save the node key, ni key and the edges weight between them.
                int nodeKey=node.getKey();
                int niKey=ni.getKey();
                double edgeWeight= _g.getEdge(node.getKey(),ni.getKey());
                //init all the data to the new graph
                g.connect(nodeKey, niKey, edgeWeight);
            }
        }
    }

    @Override
    // Returns true if and only if there is a valid path from every
    // node to each other node by use Bfs algorithm.
    //this is an algorithm who passing by all the graph's nodes.

    public boolean isConnected() {

        if(_g.getV().size()==0||_g.getV().size()==1)
            return true;

        //I use here in HashSet to save if the node curr was visited
        //and in LinkedList to put inside the neighbors of the current node.
        HashSet<Integer> visited = new HashSet<>();
        LinkedList<Integer> ls = new LinkedList<Integer>();

        // current node = visited, insert into list
        int root_node = _g.getV().iterator().next().getKey();
        visited.add(root_node);
        ls.add(root_node);

        while (ls.size() != 0) {

            // deque an entry from list and process it
            root_node = ls.poll();

            // get all adjacent nodes of current node and process.
            Iterator<node_info> i = _g.getV(root_node).iterator();
            while (i.hasNext()) {
                int n = i.next().getKey();
                if (!visited.contains(n)) {
                    visited.add(n);
                    ls.add(n);
                }
            }
        }
        return visited.size() == _g.nodeSize();
    }

    @Override
    //this method returns the length of the shortest path between src to dest
    //if no such path returns -1
    public double shortestPathDist(int src, int dest) {
        if(src==dest)
            return 0;

            //also src != dest
        else if(_g.nodeSize()==0||_g.edgeSize()==0||
                (_g.getV(src).size()==0)||(_g.getV(dest).size()==0))
            return-1;

        shortestPath( src, dest);

        //at the shortestPath method we are updating the nodes tag
        //to be the total and smallest weight from src up to them.
        //so at dest we have the total and smallest weight
        return _g.getNode(dest).getTag();
    }

    @Override
    //this method returns the the shortest path between src to dest -
    // as an ordered List of nodes: src--> n1-->n2-->...dest
    //at this method I use Dijkstra’s algorithm.

    public List<node_info> shortestPath(int src, int dest) {

        //if the node exist in the graph, and src=dest
        // than the method returns List with one node - src.

        if(src==dest&&_g.getV().contains(_g.getNode(src))){
            List <node_info> ans = new ArrayList<>();
            ans.add(_g.getNode(src));
            return ans;
        }
        //if the dest/src doesnt exist at yhe graph
        //or one of them there isn't neighbors at all , its mean that
        //the path doesnt exist and the method returns null.

        else if(!_g.getV().contains(_g.getNode(src))||
                !_g.getV().contains(_g.getNode(dest)) ||
                _g.getV(src).isEmpty()|| _g.getV(dest).isEmpty())
            return null;
        //I use here in HashSet to save if the node curr was taken care of,
        // and HashMap to save the predecessor nodes.
        HashSet<Integer> spt= new HashSet<>();
        HashMap<Integer,Integer>pred=new HashMap();

        //Initialize all the distance(tags) to infinity
        //tags used to store the distance of vertex from a source

        for(node_info n: _g.getV()){
            n.setTag(Double.MAX_VALUE);
        }

        //Initialize priority queue
        //override the comparator to do the sorting based tags.

        PriorityQueue<node_info> pq=new PriorityQueue(_g.getV().size(),
                new Comparator<node_info>() {


                    @Override
                    public int compare(node_info n1, node_info n2) {
                        //sort using biggest values
                        if(n1.getTag()> n2.getTag())
                            return 1;
                        if(n2.getTag()> n1.getTag())
                            return -1;

                        return 0;
                    }
                });
        //create the pair for for the first index, 0 distance (tag) 0 index
        _g.getNode(src).setTag(0);
        pq.add(_g.getNode(src));

        //while priority queue is not empty
        while(!pq.isEmpty()){

            //extract the min
            node_info curr = pq.poll();

            //extracted vertex
            if(spt.contains(curr.getKey())==false) {
                spt.add(curr.getKey());

                //iterate through all the adjacent vertices and update the keys
                for (node_info ni: _g.getV(curr.getKey())) {

                    //only if edge destination is not present in spt
                    if (spt.contains(ni.getKey()) == false) {

                        ///check if distance needs an update or not
                        //means check total weight from source to vertex_V is less than
                        //the current distance value, if yes then update the distance
                        double newDist =  curr.getTag() + _g.getEdge(curr.getKey(), ni.getKey()) ;

                        if(ni.getTag()>newDist){
                            ni.setTag(newDist);
                            pq.offer(ni);
                            pred.put(ni.getKey(),curr.getKey()) ;
                        }
                    }
                }
            }
        }

        List<node_info> ans= new ArrayList<>();
        int t=dest;
        //iterate through all the predecessor nodes and put them in the list.
        while(t!=src){
            //we add to the list the nodes are in the route
            ans.add(_g.getNode(t));
            //t become the node before him in the route
            t=pred.get(t);
        }
        ans.add(_g.getNode(src));
        Collections.reverse(ans);
        return ans;
    }

    @Override
    //Saves this weighted undirected graph to the given file name

    public boolean save(String file)  {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            //first we save all nodes in the graph and their data.
            for (node_info n : _g.getV()) {
                bw.write(n.getKey() + "," + n.getTag() + "," + n.getInfo() + "\n");
            }

            bw.write("neighbors: \n");
            //second we save every node neighbors, and the edges between them.
            for (node_info n : _g.getV()) {
                for (node_info ni : _g.getV(n.getKey())) {
                    bw.write("currKey: " + n.getKey()+" niKey: " + ni.getKey()
                            + " weight " + _g.getEdge(n.getKey(), ni.getKey()) + "\n");
                }
            }
            bw.close();
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("IOException");
        }
        return false;
    }

    @Override
    //This method load a graph to this graph algorithm.
    // if the file was successfully loaded - the underlying graph
    // of this class will be changed (to the loaded one), in case the
    // graph was not loaded the original graph should remain "as is".
    public boolean load(String file)  {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String curr;
            weighted_graph g= new WGraph_DS();
            curr = br.readLine();

            //were go through all the nodes keys.
            while(curr!=null && !curr.equals("neighbors: ")){
                StringTokenizer st = new StringTokenizer (curr, " , " , false);

                //the nodes key at the 1 place, the tag at 2 place,
                //the info at 3 place.

                int nodeKey=  Integer.parseInt(st.nextToken());
                //System.out.println("node key "+nodeKey); ---> test for me.
                //add the curr node to the graph by is key.
                g.addNode(nodeKey);

                //set the curr node tag .
                double nodeTag= Double.parseDouble(st.nextToken());
                //System.out.println("nodeTag "+nodeTag); ---> test for me.
                g.getNode(nodeKey).setTag(nodeTag);


                //set the  curr node info.
                g.getNode(nodeKey).setInfo(st.nextToken());

                curr= br.readLine();
            }
            //we got to the neighbors
            curr=br.readLine();

            //until we finish the txt.
            while(curr!=null){
                StringTokenizer st = new StringTokenizer (curr,
                        " " , false);
                //the nodes key at the 2 place, the ni key at 4 place,
                // the edge weight at 6 place.
                if(!st.nextToken().isEmpty()) {

                    String currKey=st.nextToken();
                    //System.out.println("currN "+currKey); --->test for me.
                    st.nextToken();
                    String niKey=st.nextToken();
                    //System.out.println("niK "+niKey);  --->test for me.
                    st.nextToken();
                    String weight=st.nextToken();
                    //System.out.println("W "+weight);  --->test for me.

                    //connect the node with the ni, and init the edge weight.
                    g.connect(Integer.parseInt(currKey), Integer.parseInt(niKey),
                            Double.parseDouble(weight));
                    curr = br.readLine();
                }
            }
            //changed the graph of this class.
            this.init(g);
            return  true;
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("IOException");

        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return Objects.equals(_g, that._g);
    }


}
