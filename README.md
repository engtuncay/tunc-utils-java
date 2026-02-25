<h2>Tunc Utility Java Library</h2>

Java Utitiliy library files.

Some packages will be removed ( com/maimart/fx/tablefilter , mark/utils,  net/coderazzi/filters )

# Contents

- [Contents](#contents)
- [Kısaltmalar](#kısaltmalar)
- [Sql Yardımcı Sınıflar](#sql-yardımcı-sınıflar)
  - [FiQugen - Fi Query Generator](#fiqugen---fi-query-generator)
- [Kod Üretme](#kod-üretme)
  - [FiCodeGen - Fi Code Generator](#ficodegen---fi-code-generator)
- [Gui Pencere Sınıfları](#gui-pencere-sınıfları)
  - [IFxSimpleCont](#ifxsimplecont)
  - [AbsFxSimpleCont](#absfxsimplecont)
- [Components](#components)
  - [Form](#form)
    - [FxFormMig](#fxformmig)
    - [FxFormMigc](#fxformmigc)
- [Entegre Mikro - Utility Sınıfları](#entegre-mikro---utility-sınıfları)
- [Entegre Fx - Utility Classes](#entegre-fx---utility-classes)
  - [FiColsCompEfx](#ficolscompefx)
  - [EfhFiColList](#efhficollist)
- [Entegre Controller Yapısı](#entegre-controller-yapısı)
  - [TuncUtils Sınıfında Gui Sınıflar](#tuncutils-sınıfında-gui-sınıflar)
    - [IFxSimpleCont](#ifxsimplecont-1)
    - [AbsFxSimpleCont](#absfxsimplecont-1)
  - [Entegre'deki Gui Sınıfları](#entegredeki-gui-sınıfları)
    - [IFxEntSimpleView View Arayüz](#ifxentsimpleview-view-arayüz)
    - [IFxEntSimpleCont Controller Arayüz](#ifxentsimplecont-controller-arayüz)
    - [AbsModBaseCont implements IFxEntSimpleCont](#absmodbasecont-implements-ifxentsimplecont)
    - [AbsMotWindowCont extends AbsModBaseCont](#absmotwindowcont-extends-absmodbasecont)



[🔝](#contents)

# Kısaltmalar

- EnhpMod : Entegre Helper Modules. örneğin : EnhpModHelper (modüllerde kullanılacak yardımcı metodlar - gui de içerir)

**EntegreFx**

- Efh : EntegreFx Helper. Örnek : EfhFiColList


# Sql Yardımcı Sınıflar

## FiQugen - Fi Query Generator

- Sorgu oluşturur (select,update,delete) (FiCol veya Reflection ile)

# Kod Üretme

## FiCodeGen - Fi Code Generator

- FiCol class gibi kodlar üretir

# Gui Pencere Sınıfları

## IFxSimpleCont

controller interface tanımı

## AbsFxSimpleCont

IFxSimpleCont'u uygulayan, tüm controllerde olması gereken alan ve metodlar. Oluşturulan controller'lar AbsFxSimpleCont extend ederek yazılır.

# Components

## Form

### FxFormMig

### FxFormMigc

config objesi ile çalışan FxFormMig


# Entegre Mikro - Utility Sınıfları

- EmhIcon : Entegre Mikro'da kullanılan ikonları tutan sınıf. (örneğin : getReportIcon() )

# Entegre Fx - Utility Classes

## FiColsCompEfx

- EntegreFx projesinde yer alır.

- FiCol kullanarak componentler oluşturulabilir. ( FxComboBox, FxListView ve EntegreFx modullerini kullanabilir. )

## EfhFiColList



# Entegre Controller Yapısı

## TuncUtils Sınıfında Gui Sınıflar

### IFxSimpleCont

controller interface tanımı

### AbsFxSimpleCont

IFxSimpleCont'u uygulayan, tüm controllerde olması gereken alan ve metodlar. Oluşturulan controller'lar AbsFxSimpleCont extend ederek yazılır.

## Entegre'deki Gui Sınıfları

### IFxEntSimpleView View Arayüz

- IFxEntSimpleView, IFxSimpleView interface extend eden ve entegre'ye özel arayüz

### IFxEntSimpleCont Controller Arayüz

### AbsModBaseCont implements IFxEntSimpleCont

Abstract Module base Sınıfı.

Bütün module pencerelerinde (controller) bulunması istenen alanlar buraya eklenir.

### AbsMotWindowCont extends AbsModBaseCont 

