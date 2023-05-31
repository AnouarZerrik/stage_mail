package stage_mail;


class Entreprise {
    private String name;
    private String activity;
    private String address;
    private String phone;
    private String email;
    private String site;
    private String city;
    
    public Entreprise(String name, String address, String phone, String email,String activity,String city, String site) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.activity=activity;
        this.city=city;
        this.site=site;
        
    }
    
    public Entreprise() {
		// TODO Auto-generated constructor stub
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
        return name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    
    public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}


	public void setaddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	
	public void setCity(String city) {
		this.city = city;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}
	

	public void setEmail(String email) {
		this.email = email;
	}


	
}