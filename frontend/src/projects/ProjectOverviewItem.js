import React from "react";
import {useHistory} from "react-router-dom";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import makeStyles from "@material-ui/core/styles/makeStyles";

export default function ProjectOverviewItem({project}) {

    const classes = useStyles();
    const history = useHistory();

    return (
        <Card className={classes.root} variant="elevation">
            <CardContent>
                <Typography variant="h5" component="h2">
                    {project.title}
                </Typography>
                <Typography className={classes.pos} color="textSecondary">
                    by {project.projectOwner}
                </Typography>
                <Typography variant="body2" component="p">
                    {project.description}
                </Typography>
                <hr/>
            </CardContent>
            <CardContent>
            <img width="100%" src="./images/man-repairing-bike-confident-young-600w-499233697.webp"/>
                <hr/>
            </CardContent>
            <CardActions className={classes.buttonPosition}>
                <Button className={classes.button} size="small" onClick={() => history.push(`/project/${project.id}`)}>Check out project</Button>
            </CardActions>
        </Card>
    );
}

const useStyles = makeStyles({
    root: {
        minWidth: 275,
        backgroundColor: "#FFF4F4",
        borderRadius: "20px"
    },
    button: {
        backgroundColor: "#9F0D2F",
        color: "white",
        borderRadius: "10px",
        fontSize: "0.9em",
        padding: "8px"
    },
    name: {
        fontSize: 20
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
    buttonPosition: {
        justifyContent: "center"
    }
});