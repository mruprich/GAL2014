#include "mainwindow.h"
#include "main.h"
/*
Int main, tak co bycaashom se tu asi tak dozvedeli... mozna to, ze jsme zprovoznili GIT v oknech?

*/

bool checkGraph(){
    std::cout<<"Graph Check OK."<<std::endl;
    return true;
}

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.show();
    checkGraph();
    return a.exec();
}
