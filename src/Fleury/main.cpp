#include "mainwindow.h"
#include "main.h"
/*
Int main, tak co bycaashom se tu asi tak dozvedeli... mozna to, ze jsme zprovoznili GIT v oknech?

*/

bool checkGraph(){
/*
    using namespace boost;
        typedef adjacency_list<vecS, vecS, directedS,property<vertex_name_t,std::string> > BoostGraphType;
        typedef dynamic_properties BoostDynamicProperties;

        std::string fn = "Test1.graphml";
        std::ifstream is(fn.c_str());
        if (!is.is_open())
        {
            std::cout << "loading file '" << fn << "'failed." << std::endl;
            throw "Could not load file.";
        }

        BoostGraphType g;
        BoostDynamicProperties dp ;
        const std::string vn = "vertex_name";
        dp.property(vn,get(vertex_name,g));
        read_graphml(is, g, dp);*/
/*
        for (vp = vertices(g); vp.first != vp.second; ++vp.first)
        {
            std::cout << "index '" << get(vertex_index,g,*vp.first) << "' ";
            std::cout << "name '" << get(vertex_name,g,*vp.first) << "'"
            << std::endl;
        }
*/
        return 0;
}

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.createActions(); //vytvori se akce prirazeny

    w.show();
    checkGraph();
    return a.exec();
}
