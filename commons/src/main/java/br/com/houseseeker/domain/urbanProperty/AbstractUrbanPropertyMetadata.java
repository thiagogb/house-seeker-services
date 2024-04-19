package br.com.houseseeker.domain.urbanProperty;

import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractUrbanPropertyMetadata {

    public abstract String getProviderCode();

    public abstract String getUrl();

    public abstract UrbanPropertyContract getContract();

    public abstract UrbanPropertyType getType();

    public abstract String getSubType();

    public abstract Integer getDormitories();

    public abstract Integer getSuites();

    public abstract Integer getBathrooms();

    public abstract Integer getGarages();

    public abstract BigDecimal getSellPrice();

    public abstract BigDecimal getRentPrice();

    public abstract BigDecimal getCondominiumPrice();

    public abstract String getCondominiumName();

    public abstract Boolean isExchangeable();

    public abstract UrbanPropertyStatus getStatus();

    public abstract Boolean isFinanceable();

    public abstract Boolean isOccupied();

    public abstract String getNotes();

    public abstract List<String> getConveniences();

    public abstract String getState();

    public abstract String getCity();

    public abstract String getDistrict();

    public abstract String getZipCode();

    public abstract String getStreetName();

    public abstract Integer getStreetNumber();

    public abstract String getComplement();

    public abstract BigDecimal getLatitude();

    public abstract BigDecimal getLongitude();

    public abstract BigDecimal getTotalArea();

    public abstract BigDecimal getPrivateArea();

    public abstract BigDecimal getUsableArea();

    public abstract BigDecimal getTerrainTotalArea();

    public abstract BigDecimal getTerrainFront();

    public abstract BigDecimal getTerrainBack();

    public abstract BigDecimal getTerrainLeft();

    public abstract BigDecimal getTerrainRight();

    public abstract String getAreaUnit();

    public abstract List<AbstractUrbanPropertyMediaData> getMedias();

    @Override
    public abstract String toString();

}
