package com.netflix.vms.transformer.hollowoutput;


public class DeploymentIntent implements Cloneable {

    public int profileId = java.lang.Integer.MIN_VALUE;
    public int bitrate = java.lang.Integer.MIN_VALUE;
    public ISOCountry country = null;

    public boolean equals(Object other) {
        if(other == this)  return true;
        if(!(other instanceof DeploymentIntent))
            return false;

        DeploymentIntent o = (DeploymentIntent) other;
        if(o.profileId != profileId) return false;
        if(o.bitrate != bitrate) return false;
        if(o.country == null) {
            if(country != null) return false;
        } else if(!o.country.equals(country)) return false;
        return true;
    }

    public int hashCode() {
        int hashCode = 1;
        hashCode = hashCode * 31 + profileId;
        hashCode = hashCode * 31 + bitrate;
        hashCode = hashCode * 31 + (country == null ? 1237 : country.hashCode());
        return hashCode;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("DeploymentIntent{");
        builder.append("profileId=").append(profileId);
        builder.append(",bitrate=").append(bitrate);
        builder.append(",country=").append(country);
        builder.append("}");
        return builder.toString();
    }

    public DeploymentIntent clone() {
        try {
            DeploymentIntent clone = (DeploymentIntent)super.clone();
            clone.__assigned_ordinal = -1;
            return clone;
        } catch (CloneNotSupportedException cnse) { throw new RuntimeException(cnse); }
    }

    @SuppressWarnings("unused")
    private int __assigned_ordinal = -1;
}