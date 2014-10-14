#-------------------------------------------------
#
# Project created by QtCreator 2014-10-10T12:30:08
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = Fleury
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp

HEADERS  += mainwindow.h \
    main.h

FORMS    += mainwindow.ui

INCLUDEPATH += ../boost_1_56_0/
LIBS += "-L../boost_1_56_0/"
