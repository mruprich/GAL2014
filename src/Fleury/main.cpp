#include "mainwindow.h"
#include <QApplication>
/*
Int main, tak co bycshom se tu asi tak dozvedeli... mozna to, ze jsme zprovoznili GIT v oknech?

*/
int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.show();

    return a.exec();
}
